package com.richmat.mytuya.ui.sign

import com.richmat.mytuya.ui.domain.model.User

data class UserLoginState(
    val users: List<User> = emptyList(),
    val isClickable: Boolean = false,
)