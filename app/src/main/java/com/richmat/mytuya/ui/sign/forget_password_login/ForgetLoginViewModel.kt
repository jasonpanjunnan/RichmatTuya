package com.richmat.mytuya.ui.sign.forget_password_login

import android.widget.Toast
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.richmat.mytuya.MyApplication
import com.richmat.mytuya.ui.domain.use_case.UserUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ForgetLoginViewModel @Inject constructor(
    private val userUseCase: UserUseCase,
    savedStateHandle: SavedStateHandle,
) : ViewModel() {
    val phone = savedStateHandle.get<String>("phone")!!
    private val countryCode = savedStateHandle.get<String>("countryCode")!!
//    private val _phone = mutableStateOf("")
//    val phone: State<String> = _phone

//    private val _countryCode = mutableStateOf("86")
//    val countryCode: State<String> = _countryCode

    fun onEvent(event: ForgetLoginEvent) {
        when (event) {
            is ForgetLoginEvent.EnterPhone -> {
//                _phone.value = event.phone
            }
            is ForgetLoginEvent.ForgetLogin -> {
                if (event.code.length != 6) {
                    Toast.makeText(MyApplication.context,
                        "验证码数量错误！",
                        Toast.LENGTH_SHORT).show()
                } else {
                    viewModelScope.launch {

                        val login = userUseCase.forgetLogin(countryCode = countryCode,
                            phone = phone,
                            code = event.code)
                        if (login) {
                            event.navigate()
                        } else {
                            Toast.makeText(MyApplication.context,
                                "验证码错误！",
                                Toast.LENGTH_SHORT).show()
                        }
                    }
                }
            }
        }
    }
}