package com.richmat.mytuya.ui.sign.textFieldState

import com.richmat.mytuya.ui.sign.textFieldState.TextFieldState
import java.util.regex.Pattern

class PhoneState :
    TextFieldState(validator = ::isPhoneNumberValid, errorFor = ::phoneNumberValidationError)

//private fun isPhoneNumberValid(number: String):Boolean = Pattern.matches("",number)

fun isPhoneNumberValid(phone: String): Boolean {
    val compile = Pattern.compile("^(13|14|15|16|17|18|19)\\d{9}$")
    val matcher = compile.matcher(phone)
    return matcher.matches()
}

private fun phoneNumberValidationError(email: String): String {
    return "非法手机号: $email"
}