package com.richmat.mytuya.ui.domain.use_case

import com.richmat.mytuya.ui.domain.model.User
import com.richmat.mytuya.ui.domain.repository.UserRepository
import kotlinx.coroutines.flow.Flow

class GetUsers(
    private val repository: UserRepository,
) {
    operator fun invoke(): Flow<List<User>> {
        return repository.getUsers()
//            .map {  }
    }
}