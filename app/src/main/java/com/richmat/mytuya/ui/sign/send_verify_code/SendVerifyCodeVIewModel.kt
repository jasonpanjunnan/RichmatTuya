package com.richmat.mytuya.ui.sign.send_verify_code

import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.richmat.mytuya.ui.domain.use_case.UserUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SendVerifyCodeViewModel @Inject constructor(
    private val userUseCase: UserUseCase,
) : ViewModel() {
    private val _phone = mutableStateOf("")
    val phone: State<String> = _phone

    private val _countryCode = mutableStateOf("86")
    val countryCode: State<String> = _countryCode

    fun onEvent(event: SendVerifyCodeEvent) {
        when (event) {
            is SendVerifyCodeEvent.EnterPhone -> {
                _phone.value = event.phone
            }
            is SendVerifyCodeEvent.SendVerifyCodeWithUserName -> {
                viewModelScope.launch {
                    val isSend = userUseCase.sendVerifyCode("", phone.value, countryCode.value, 2)
                    if (isSend) {
                        event.navigate()
                    }
                }
            }
            is SendVerifyCodeEvent.EnterCountryCode -> {
                _countryCode.value = event.countryCode
            }
        }
    }
}