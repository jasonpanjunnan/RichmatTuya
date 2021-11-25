package com.richmat.mytuya.ui.sign.sign_in

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
class SignInViewModel @Inject constructor(
    private val userUseCase: UserUseCase,
) : ViewModel() {

    val currentCountry = userUseCase.observeCountry().stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5000),
        initialValue = CountryRespBean().default()
    )

    fun onEvent(event: SignInEvent) {
        when (event) {
            is SignInEvent.TouristRegisterAndLogin -> {
                viewModelScope.launch {
                    if (userUseCase.touristRegisterAndLogin(event.countryCode)) {
                        event.navigate()
                    }
                }
            }
        }
    }
}