package com.richmat.mytuya.ui.domain.use_case

import com.richmat.mytuya.ui.domain.repository.UserRepository


class Register(
    private val repository: UserRepository,
) {
    suspend operator fun invoke(
        countryCode: String,
        phone: String,
        password: String,
        code: String,
    ): Boolean {
        return try {
            repository.register(
                countryCode,
                phone,
                password,
                code,
            )
        } catch (e: Exception) {
            e.printStackTrace()
            false
        }
    }
}