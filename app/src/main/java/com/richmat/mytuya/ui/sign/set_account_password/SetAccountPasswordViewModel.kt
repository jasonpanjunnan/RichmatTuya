package com.richmat.mytuya.ui.sign.set_account_password

import android.util.Log
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.richmat.mytuya.ui.domain.use_case.UserUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SetAccountPasswordViewModel @Inject constructor(
    private val userUseCase: UserUseCase,
    savedStateHandle: SavedStateHandle,
) : ViewModel() {
    val phone = savedStateHandle.get<String>("phone")!!
    private val countryCode = savedStateHandle.get<String>("countryCode")!!
    private val code = savedStateHandle.get<String>("verify_code")!!

    init {
        Log.e("TAG", ": $phone ,$countryCode ,$code")
    }

    fun register(
        password: String,
        navigate: () -> Unit,
    ) {
        viewModelScope.launch {
            val result = userUseCase.register(
                countryCode,
                phone,
                password,
                code)
            if (result) navigate()
        }
    }
}