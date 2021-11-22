package com.richmat.mytuya.ui.sign.send_verify_code

sealed class SendVerifyCodeEvent {
    data class EnterPhone(val phone: String) : SendVerifyCodeEvent()
    data class EnterCountryCode(val countryCode: String) : SendVerifyCodeEvent()
    data class SendVerifyCodeWithUserName(val navigate: () -> Unit) : SendVerifyCodeEvent()
}
