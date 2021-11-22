package com.richmat.mytuya.ui.sign

import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.richmat.mytuya.ui.domain.model.User
import com.richmat.mytuya.ui.domain.use_case.UserUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class LoginViewModel @Inject constructor(
    private val userUseCase: UserUseCase,
) : ViewModel() {
    private val _state = MutableStateFlow(UserLoginState())
    val state: StateFlow<UserLoginState> = _state

    private val _phone = mutableStateOf<String>("")
    val phone: State<String> = _phone

    private val _password = mutableStateOf("")
    val password: State<String> = _password

    private val _countryCode = mutableStateOf("86")
    val countryCode: State<String> = _countryCode

    private var getUserJob: Job? = null

    init {
        getUsers()
    }

    private fun getUsers() {
        getUserJob?.cancel()
        getUserJob = userUseCase.getUsers().onEach { users ->
            var myUsers = users
            if (users.isEmpty()) {
                myUsers = listOf(User(password = "",
                    phone = "", name = "", id = 1,
                    countryCode = "86"))
                userUseCase.insertUser(myUsers[0])
            }
            _state.update { it.copy(users = myUsers) }
        }.launchIn(viewModelScope)
    }

    fun onLoginEvent(event: LoginEvent) {
        when (event) {
            is LoginEvent.Login -> {
                viewModelScope.launch {
                    userUseCase.login(_countryCode.value,
                        _phone.value,
                        _password.value,
                        event.navigate)
                }
            }
            is LoginEvent.EnterCountryCode -> {
                _countryCode.value = event.countryCode
            }
            is LoginEvent.EnterPassword -> {
                _password.value = event.password
            }
            is LoginEvent.EnterPhone -> {
                _phone.value = event.phone
            }
            LoginEvent.ForgetPassWord -> {

            }
        }
    }
}