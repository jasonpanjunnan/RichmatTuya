package com.richmat.mytuya.ui.domain.use_case

import com.richmat.mytuya.ui.domain.repository.UserRepository

class ForgetLogin(
    private val repository: UserRepository
) {
    suspend operator fun invoke(){

    }
}