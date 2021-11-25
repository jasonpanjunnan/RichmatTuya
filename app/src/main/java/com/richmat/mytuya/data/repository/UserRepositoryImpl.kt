package com.richmat.mytuya.data.repository

import android.os.Build
import android.provider.Settings
import android.util.Log
import android.widget.Toast
import androidx.annotation.RequiresApi
import com.richmat.mytuya.MyApplication
import com.richmat.mytuya.data.data_source.UserDao
import com.richmat.mytuya.ui.domain.model.User
import com.richmat.mytuya.ui.domain.repository.UserRepository
import com.richmat.mytuya.util.default
import com.tuya.smart.android.base.bean.CountryRespBean
import com.tuya.smart.android.user.api.*
import com.tuya.smart.android.user.bean.WhiteList
import com.tuya.smart.home.sdk.TuyaHomeSdk
import com.tuya.smart.sdk.api.IResultCallback
import com.tuya.smart.sdk.api.ITuyaDataCallback
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.sync.Mutex
import kotlinx.coroutines.sync.withLock
import java.io.File
import kotlin.coroutines.resume
import kotlin.coroutines.resumeWithException
import kotlin.coroutines.suspendCoroutine


class UserRepositoryImpl(
    private val dao: UserDao,
) : UserRepository {
    // for now, keep the selections in memory
    private val currentCountry = MutableStateFlow(CountryRespBean().default())

    // Used to make suspend functions that read and update state safe to call from any thread
    private val mutex = Mutex()

    override fun getUsers(): Flow<List<User>> {
        return dao.getUsers()
    }

    override suspend fun getUserById(id: Int): User? {
        return dao.getUserById(id)
    }

    override suspend fun insertUser(user: User) {
        dao.insertUser(user)
    }

    override suspend fun deleteUser(user: User) {
        dao.deleteUser(user)
    }

    override suspend fun login(
        countryCode: String,
        phone: String,
        password: String,
        navigate: () -> Unit,
    ) {
        TuyaHomeSdk.getUserInstance()
            .loginWithPhonePassword(
                countryCode,
                phone,
                password,
                object : ILoginCallback {
                    override fun onSuccess(user: com.tuya.smart.android.user.bean.User?) {
                        navigate()
                    }

                    override fun onError(code: String?, error: String?) {
                        Toast.makeText(
                            MyApplication.context,
                            "code: " + code + "error: $error, /n 账号或密码错误，请重试",
                            Toast.LENGTH_SHORT
                        ).show()
                    }
                }
            )
    }

    override suspend fun forgetLogin(countryCode: String, phone: String, code: String): Boolean {
// 手机验证码登录
        return suspendCoroutine { continuation ->
            TuyaHomeSdk.getUserInstance()
                .loginWithPhone(countryCode, phone, code, object : ILoginCallback {
                    override fun onSuccess(user: com.tuya.smart.android.user.bean.User?) {
                        Toast.makeText(MyApplication.context,
                            "登录成功，用户名：" + TuyaHomeSdk.getUserInstance().user!!.username,
                            Toast.LENGTH_SHORT).show()
                        continuation.resume(true)
                    }

                    override fun onError(code: String, error: String) {
                        Toast.makeText(MyApplication.context, error, Toast.LENGTH_SHORT).show()
                        continuation.resumeWithException(Exception("$code////$error"))
                    }
                })
        }
    }

    override suspend fun sendVerifyCodeWithUserName(
        region: String,
        phone: String,
        countryCode: String,
        type: Int,
    ): Boolean {
        return suspendCoroutine { continuation ->
            TuyaHomeSdk.getUserInstance().sendVerifyCodeWithUserName(
                phone,
                region, countryCode, type, object : IResultCallback {
                    override fun onError(code: String?, error: String?) {
                        Toast.makeText(MyApplication.context,
                            error,
                            Toast.LENGTH_SHORT).show()
                        continuation.resumeWithException(Exception("$code////$error"))
                    }

                    override fun onSuccess() {
                        continuation.resume(true)
                    }
                })
        }
    }

    override suspend fun checkRegionSupported(): Boolean {
        return suspendCoroutine { continuation ->
            TuyaHomeSdk.getUserInstance()
                .getWhiteListWhoCanSendMobileCodeSuccess(object : IWhiteListCallback {
                    override fun onSuccess(whiteList: WhiteList) {
                        continuation.resume(true)
                    }

                    override fun onError(code: String, error: String) {
                        Toast.makeText(MyApplication.context,
                            "该地区不支持：" + "code: " + code + "error:" + error,
                            Toast.LENGTH_SHORT)
                            .show()
                        continuation.resumeWithException(Exception("$code////$error"))
                    }
                })
        }
    }

    override fun getTuyaUser(): com.tuya.smart.android.user.bean.User? {
        return TuyaHomeSdk.getUserInstance().user
    }

    override suspend fun getTuyaUserByUpdate(): com.tuya.smart.android.user.bean.User? {
        return if (updateUserInfo()) {
            getTuyaUser()
        } else {
            null
        }
    }

    override suspend fun updateUserInfo(): Boolean {
        return suspendCoroutine { continuation ->
            TuyaHomeSdk.getUserInstance().updateUserInfo(object : IResultCallback {
                override fun onError(code: String, error: String) {
                    continuation.resumeWithException(Exception("$code////$error"))
                }

                override fun onSuccess() {
                    continuation.resume(true)
                }
            })
        }
    }

    override suspend fun getCountries(): String {
        return suspendCoroutine { continuation ->
            //插值器，应该可以根据输入获取想要的数据
            val postData: Map<String, Any>? = null

            TuyaHomeSdk.getRequestInstance()
                .requestWithApiNameWithoutSession("tuya.m.country.list", "1.0", postData,
                    String::class.java, object : ITuyaDataCallback<String> {
                        override fun onSuccess(result: String) {
                            Log.i("TAG", result)
                            continuation.resume(result)
                        }

                        override fun onError(errorCode: String, errorMessage: String) {
                            Log.i("TAG", errorCode)
                            continuation.resumeWithException(Exception(errorMessage))
                        }
                    })
        }
    }

    override suspend fun selectedCurrentCountry(country: CountryRespBean) {
        mutex.withLock {
            currentCountry.value = country
            Log.e("TAG",
                "selectedCurrentCountry: ${currentCountry.value},${currentCountry.value.n}")
        }
    }

    override fun observeCountry(): Flow<CountryRespBean> = currentCountry

    override suspend fun register(
        countryCode: String,
        phone: String,
        password: String,
        code: String,
    ): Boolean {
        return suspendCoroutine { continuation ->
            // 注册手机密码账户
            TuyaHomeSdk.getUserInstance().registerAccountWithPhone(
                countryCode,
                phone,
                password,
                code,
                object : IRegisterCallback {
                    override fun onSuccess(user: com.tuya.smart.android.user.bean.User) {
                        continuation.resume(true)
                        Toast.makeText(MyApplication.context, "注册成功", Toast.LENGTH_SHORT).show()
                    }

                    override fun onError(code: String, error: String) {
                        continuation.resumeWithException(Exception("$code////$error"))
                        Toast.makeText(
                            MyApplication.context,
                            "code: " + code + "error:" + error,
                            Toast.LENGTH_SHORT
                        ).show()
                    }
                })
        }
    }

    @RequiresApi(Build.VERSION_CODES.N_MR1)
    override suspend fun touristRegisterAndLogin(countryCode: String): Boolean {
//                游客账号登陆,获取手机名称作为游客名称。华为、小米等手机获取到的是手机型号
        val request = Settings.Global.getString(MyApplication.context.contentResolver,
            Settings.Global.DEVICE_NAME)
        Log.e("TAG", "touristRegisterAndLogin: $request")

        return suspendCoroutine { continuation ->
            TuyaHomeSdk.getUserInstance().touristRegisterAndLogin(countryCode, request,
                object : IRegisterCallback {
                    override fun onSuccess(user: com.tuya.smart.android.user.bean.User) {
                        Toast.makeText(MyApplication.context, "onSuccess:", Toast.LENGTH_SHORT)
                            .show()
                        Log.e("TAG", "游客登录成功:${user.nickName} ")
                        continuation.resume(true)
                    }

                    override fun onError(code: String?, error: String?) {
                        Toast.makeText(MyApplication.context,
                            "onError: $code, $error",
                            Toast.LENGTH_SHORT)
                            .show()
                        continuation.resumeWithException(Exception("$code////$error"))
                        Log.e("TAG", "onError: $code, $error")
                    }
                })
        }
    }

    override suspend fun updateNickName(nickName: String): Boolean {
        return suspendCoroutine { continuation ->
            TuyaHomeSdk.getUserInstance().updateNickName(nickName,
                object : IReNickNameCallback {
                    override fun onSuccess() {
                        continuation.resume(true)
                    }

                    override fun onError(code: String, error: String) {
                        continuation.resumeWithException(Exception("$code////$error"))
                    }
                })
        }
    }

    override suspend fun uploadUserAvatar(file: File): Boolean {
        return suspendCoroutine { continuation ->
            TuyaHomeSdk.getUserInstance().uploadUserAvatar(
                file,
                object : IBooleanCallback {
                    override fun onSuccess() {
                        continuation.resume(true)
                    }

                    override fun onError(code: String, error: String) {
                        continuation.resumeWithException(Exception("$code////$error"))
                    }
                })
        }
    }
}