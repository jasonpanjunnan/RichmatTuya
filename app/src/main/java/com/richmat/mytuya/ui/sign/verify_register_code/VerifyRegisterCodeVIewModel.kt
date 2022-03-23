package com.richmat.mytuya.ui.sign.verify_register_code

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
class VerifyRegisterCodeViewModel @Inject constructor(
    private val userUseCase: UserUseCase,
    savedStateHandle: SavedStateHandle,
) : ViewModel() {
    val phone = savedStateHandle.get<String>("phone")!!
    val countryCode = savedStateHandle.get<String>("countryCode")!!
//    private val _phone = mutableStateOf("")
//    val phone: State<String> = _phone

//    private val _countryCode = mutableStateOf("86")
//    val countryCode: State<String> = _countryCode

    fun onEvent(event: VerifyRegisterCodeEvent) {
        when (event) {
            is VerifyRegisterCodeEvent.EnterPhone -> {
//                _phone.value = event.phone
            }
            is VerifyRegisterCodeEvent.ForgetLogin -> {
                if (event.code.length != 6) {
                    Toast.makeText(MyApplication.context,
                        "验证码数量错误！",
                        Toast.LENGTH_SHORT).show()
                } else {
                    viewModelScope.launch {
                        val login = userUseCase.forgetLogin(
                            countryCode = countryCode,
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