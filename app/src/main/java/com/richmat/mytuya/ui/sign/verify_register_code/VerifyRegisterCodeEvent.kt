package com.richmat.mytuya.ui.sign.verify_register_code

sealed class VerifyRegisterCodeEvent {
    data class EnterPhone(val phone: String) : VerifyRegisterCodeEvent()
    data class ForgetLogin(val code: String, val navigate: () -> Unit) : VerifyRegisterCodeEvent()
}