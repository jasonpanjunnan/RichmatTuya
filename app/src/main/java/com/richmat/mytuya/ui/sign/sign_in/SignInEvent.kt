package com.richmat.mytuya.ui.sign.sign_in

sealed class SignInEvent {
    data class TouristRegisterAndLogin(val countryCode: String, val navigate: () -> Unit) :
        SignInEvent()
}
