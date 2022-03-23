package com.richmat.mytuya.ui.domain.use_case

import com.richmat.mytuya.ui.domain.repository.UserRepository

class UpdateNickName(
    private val repository: UserRepository,
) {
    suspend operator fun invoke(nickName: String): Boolean {
        return try {
            repository.updateNickName(nickName = nickName)
        } catch (e: Exception) {
            e.printStackTrace()
            false
        }
    }
}