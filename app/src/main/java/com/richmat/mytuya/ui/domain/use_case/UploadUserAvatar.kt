package com.richmat.mytuya.ui.domain.use_case

import com.richmat.mytuya.ui.domain.repository.UserRepository
import java.io.File

class UploadUserAvatar(
    private val repository: UserRepository,
) {
    suspend operator fun invoke(file: File): Boolean {
        return try {
            repository.uploadUserAvatar(file)
        } catch (e: Exception) {
            e.printStackTrace()
            false
        }
    }
}