package com.richmat.mytuya.ui.domain.use_case

import com.richmat.mytuya.ui.domain.repository.UserRepository
import com.tuya.smart.android.user.bean.User

class GetTuyaUser(
    private val repository: UserRepository,
) {
    operator fun invoke(): User? {
        return repository.getTuyaUser()
    }
}