package com.richmat.mytuya.data.repository

import android.widget.Toast
import com.richmat.mytuya.MyApplication
import com.richmat.mytuya.data.data_source.UserDao
import com.richmat.mytuya.ui.domain.model.User
import com.richmat.mytuya.ui.domain.repository.UserRepository
import com.tuya.smart.android.user.api.ILoginCallback
import com.tuya.smart.android.user.api.IWhiteListCallback
import com.tuya.smart.android.user.bean.WhiteList
import com.tuya.smart.home.sdk.TuyaHomeSdk
import com.tuya.smart.sdk.api.IResultCallback
import kotlinx.coroutines.flow.Flow


class UserRepositoryImpl(
    private val dao: UserDao,
) : UserRepository {
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

    override suspend fun forgetLogin(countryCode: String, phone: String) {
        TuyaHomeSdk.getUserInstance()
            .getWhiteListWhoCanSendMobileCodeSuccess(object : IWhiteListCallback {
                override fun onSuccess(whiteList: WhiteList) {
                    Toast.makeText(MyApplication.context,
                        "查询可用性成功:" + whiteList.countryCodes,
                        Toast.LENGTH_SHORT).show()
                }

                override fun onError(code: String, error: String) {
                    Toast.makeText(MyApplication.context,
                        "code: " + code + "error:" + error,
                        Toast.LENGTH_SHORT)
                        .show()
                }
            })
    }

    override suspend fun sendVerifyCodeWithUserName(
        region: String,
        phone: String,
        countryCode: String,
        type: Int,
    ) {
        TuyaHomeSdk.getUserInstance().sendVerifyCodeWithUserName(
            phone,
            region, countryCode, type, object : IResultCallback {
                override fun onError(code: String?, error: String?) {

                }

                override fun onSuccess() {

                }

            });
    }

    override suspend fun checkRegionSupported() {
        TuyaHomeSdk.getUserInstance()
            .getWhiteListWhoCanSendMobileCodeSuccess(object : IWhiteListCallback {
                override fun onSuccess(whiteList: WhiteList) {
                    Toast.makeText(MyApplication.context,
                        "查询可用性成功:" + whiteList.countryCodes,
                        Toast.LENGTH_SHORT).show()
                }

                override fun onError(code: String, error: String) {
                    Toast.makeText(MyApplication.context,
                        "code: " + code + "error:" + error,
                        Toast.LENGTH_SHORT)
                        .show()
                }
            })
    }
}