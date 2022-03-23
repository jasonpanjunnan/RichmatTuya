package com.richmat.mytuya.ui.domain.use_case

import com.richmat.mytuya.ui.domain.repository.UserRepository
import com.tuya.smart.android.user.bean.User

class GetTuyaUserByUpdate(
    private val repository: UserRepository,
) {
    suspend operator fun invoke(): User? {
        return try {
            repository.getTuyaUserByUpdate()
        } catch (e: Exception) {
            null
        }
    }
}