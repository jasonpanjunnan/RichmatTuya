package com.richmat.mytuya.ui.domain.use_case

import com.richmat.mytuya.ui.domain.model.User
import com.richmat.mytuya.ui.domain.repository.UserRepository

class InsertUser(
    private val repository: UserRepository,
) {
    suspend operator fun invoke(user: User) {
        repository.insertUser(user)
    }
}