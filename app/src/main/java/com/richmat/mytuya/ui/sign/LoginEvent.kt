package com.richmat.mytuya.ui.sign

sealed class LoginEvent {
    data class Login(val navigate: () -> Unit) : LoginEvent()
    data class EnterCountryCode(val countryCode: String) : LoginEvent()
    data class EnterPhone(val phone: String) : LoginEvent()
    data class EnterPassword(val password: String) : LoginEvent()
    object ForgetPassWord : LoginEvent()
}