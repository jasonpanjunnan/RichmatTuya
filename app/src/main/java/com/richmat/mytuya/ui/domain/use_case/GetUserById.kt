package com.richmat.mytuya.ui.domain.use_case

import com.richmat.mytuya.ui.domain.model.User
import com.richmat.mytuya.ui.domain.repository.UserRepository

class GetUserById(
    private val repository: UserRepository,
) {
    suspend operator fun invoke(
        id: Int,
    ): User? {
        return repository.getUserById(id)
    }
}