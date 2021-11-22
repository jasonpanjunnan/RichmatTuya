package com.richmat.mytuya.ui.domain.use_case

import com.richmat.mytuya.ui.domain.model.User
import com.richmat.mytuya.ui.domain.repository.UserRepository

class Login(
    private val repository: UserRepository,
) {
    suspend operator fun invoke(
        countryCode: String,
        phone: String,
        password: String, navigate: () -> Unit,
    ) {
        repository.login(countryCode, phone, password, navigate)
    }
}