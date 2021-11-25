package com.richmat.mytuya.ui.sign.register

sealed class RegisterEvent {
    data class EnterPhone(val phone: String) : RegisterEvent()
    data class SendVerifyCodeWithUserName(
        val region: String = "",
        val phone: String,
        val countryCode: String,
        val type: Int,
        val navigate: () -> Unit,
    ) : RegisterEvent()
}