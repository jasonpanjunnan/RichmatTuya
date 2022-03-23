package com.richmat.mytuya.ui.sign.forget_password_login

sealed class ForgetLoginEvent {
    data class EnterPhone(val phone: String) : ForgetLoginEvent()
    data class ForgetLogin(val code: String, val navigate: () -> Unit) : ForgetLoginEvent()
}