package com.richmat.mytuya.ui.domain.use_case

import com.richmat.mytuya.ui.domain.repository.UserRepository

class ForgetLogin(
    private val repository: UserRepository,
) {
    suspend operator fun invoke(countryCode: String, phone: String, code: String): Boolean {
        return try {
            repository.forgetLogin(countryCode, phone, code)
        } catch (e: Exception) {
            e.printStackTrace()
            false
        }
    }
}