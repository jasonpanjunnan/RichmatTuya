package com.richmat.mytuya.ui.sign.send_verify_code

sealed class SendVerifyCodeEvent {
    data class EnterPhone(val phone: String) : SendVerifyCodeEvent()
    data class SendVerifyCodeWithUserName(
        val region: String = "",
        val phone: String,
        val countryCode: String,
        val type: Int,
        val navigate: () -> Unit,
    ) : SendVerifyCodeEvent()
}
