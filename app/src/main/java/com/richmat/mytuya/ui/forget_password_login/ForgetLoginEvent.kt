package com.richmat.mytuya.ui.forget_password_login

sealed class ForgetLoginEvent {
    data class EnterPhone(val phone: String) : ForgetLoginEvent()
    data class GetVerificationCode(val phone: String, val countryCode: String) : ForgetLoginEvent()
}