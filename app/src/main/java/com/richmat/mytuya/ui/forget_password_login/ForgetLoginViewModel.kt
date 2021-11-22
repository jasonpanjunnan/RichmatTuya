package com.richmat.mytuya.ui.forget_password_login

import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import com.richmat.mytuya.ui.domain.use_case.UserUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import javax.inject.Inject

@HiltViewModel
class ForgetLoginViewModel @Inject constructor(
    private val userUseCase: UserUseCase
) : ViewModel() {
    private val _phone = MutableStateFlow("")
    val phone: StateFlow<String> = _phone

    private val _countryCode = mutableStateOf("86")
    val countryCode: State<String> = _countryCode

    fun onEvent(event: ForgetLoginEvent){
        when (event) {
            is ForgetLoginEvent.EnterPhone -> {
                _phone.value = event.phone
            }
            is ForgetLoginEvent.GetVerificationCode -> {
                userUseCase.forgetLogin
            }
        }
    }
}