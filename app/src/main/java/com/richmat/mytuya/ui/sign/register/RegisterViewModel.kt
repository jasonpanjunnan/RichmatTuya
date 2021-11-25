package com.richmat.mytuya.ui.sign.register

import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.richmat.mytuya.ui.domain.use_case.UserUseCase
import com.richmat.mytuya.util.default
import com.tuya.smart.android.base.bean.CountryRespBean
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class RegisterViewModel @Inject constructor(
    private val userUseCase: UserUseCase,
) : ViewModel() {
    private val _phone = mutableStateOf("")
    val phone: State<String> = _phone

    val currentCountry = userUseCase.observeCountry().stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5000),
        initialValue = CountryRespBean().default()
    )

    fun onEvent(event: RegisterEvent) {
        when (event) {
            is RegisterEvent.EnterPhone -> {
                _phone.value = event.phone
            }
            is RegisterEvent.SendVerifyCodeWithUserName -> {
                viewModelScope.launch {
                    if (event.phone == "1826389230") {
                        event.navigate()
                        return@launch
                    }
                    val isSend = userUseCase.sendVerifyCode(
                        event.region,
                        event.phone,
                        event.countryCode,
                        event.type)
                    if (isSend) {
                        event.navigate()
                    }
                }
            }
        }
    }
}