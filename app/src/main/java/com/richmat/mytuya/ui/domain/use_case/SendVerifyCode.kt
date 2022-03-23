package com.richmat.mytuya.ui.domain.use_case

import com.richmat.mytuya.ui.domain.repository.UserRepository

class SendVerifyCode(
    private val repository: UserRepository,
) {
    suspend operator fun invoke(
        region: String = "",
        phone: String,
        countryCode: String,
        type: Int,
    ): Boolean {
        try {
            val isSupport = repository.checkRegionSupported()
            if (isSupport) {
                return repository.sendVerifyCodeWithUserName(region, phone, countryCode, type)
            }
        } catch (e: Exception) {
            e.printStackTrace()
            return false
        }
        return true
    }
}