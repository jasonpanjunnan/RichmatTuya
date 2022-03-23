package com.richmat.mytuya.ui.domain.repository

import com.richmat.mytuya.ui.domain.model.User
import com.tuya.smart.android.base.bean.CountryRespBean
import kotlinx.coroutines.flow.Flow
import java.io.File

interface UserRepository {
    fun getUsers(): Flow<List<User>>

    suspend fun getUserById(id: Int): User?

    suspend fun insertUser(user: User)

    suspend fun deleteUser(user: User)

    suspend fun login(
        countryCode: String,
        phone: String,
        password: String, navigate: () -> Unit,
    )

    suspend fun forgetLogin(countryCode: String, phone: String, code: String): Boolean

    /**
     * 校验类型。取值：
    1：使用手机号码注册账号时，校验验证码
    2：使用手机号码登录账号时，校验验证码
    3：重置手机号码注册的账号的密码时，校验验证码
    8：注销手机号码注册的账号时，校验验证码

    region	区域，默认填写：“” 即可
     */
    suspend fun sendVerifyCodeWithUserName(
        region: String = "",
        phone: String,
        countryCode: String,
        type: Int,
    ): Boolean

    suspend fun checkRegionSupported(): Boolean

    suspend fun getCountries(): String

    suspend fun selectedCurrentCountry(country: CountryRespBean)

    fun observeCountry(): Flow<CountryRespBean>

    suspend fun register(
        countryCode: String,
        phone: String,
        password: String,
        code: String,
    ): Boolean

    suspend fun touristRegisterAndLogin(countryCode: String): Boolean

    suspend fun updateNickName(nickName: String): Boolean

    suspend fun uploadUserAvatar(file: File): Boolean

    fun getTuyaUser():com.tuya.smart.android.user.bean.User?

    suspend fun getTuyaUserByUpdate():com.tuya.smart.android.user.bean.User?

    suspend fun updateUserInfo():Boolean
}