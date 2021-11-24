package com.richmat.mytuya.ui.sign

sealed class LoginEvent {
    data class Login(
        val countryCode: String,
        val phone: String,
        val password: String,
        val navigate: () -> Unit,
    ) : LoginEvent()

    data class EnterPhone(val phone: String) : LoginEvent()
    data class EnterPassword(val password: String) : LoginEvent()
    object ForgetPassWord : LoginEvent()
}