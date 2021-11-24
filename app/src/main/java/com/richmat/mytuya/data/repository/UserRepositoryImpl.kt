package com.richmat.mytuya.data.repository

import android.util.Log
import android.widget.Toast
import com.richmat.mytuya.MyApplication
import com.richmat.mytuya.data.data_source.UserDao
import com.richmat.mytuya.ui.domain.model.User
import com.richmat.mytuya.ui.domain.repository.UserRepository
import com.richmat.mytuya.util.default
import com.tuya.smart.android.base.bean.CountryRespBean
import com.tuya.smart.android.user.api.ILoginCallback
import com.tuya.smart.android.user.api.IWhiteListCallback
import com.tuya.smart.android.user.bean.WhiteList
import com.tuya.smart.home.sdk.TuyaHomeSdk
import com.tuya.smart.sdk.api.IResultCallback
import com.tuya.smart.sdk.api.ITuyaDataCallback
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.sync.Mutex
import kotlinx.coroutines.sync.withLock
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
}