package com.richmat.mytuya.ui.Home.personal_setting

import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.richmat.mytuya.ui.domain.use_case.UserUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class PersonalSettingViewModel @Inject constructor(
    private val userUseCase: UserUseCase,
) : ViewModel() {
    private val _dialogNickName = mutableStateOf("")
    val dialogNickName: State<String> = _dialogNickName

    private val _state = MutableStateFlow(PersonalSettingState())
    val state: StateFlow<PersonalSettingState> = _state

    init {
        viewModelScope.launch {
            userUseCase.getTuyaUser()?.let { user ->
                _dialogNickName.value = user.nickName
                _state.update {
                    it.copy(nickName = user.nickName,
                        headPic = user.headPic,
                        timeZone = user.timezoneId)
                }
            }
            //在查看用户信息时，调用同步接口
//            _nickName.value = userUseCase.get
        }
    }

    fun onEvent(event: PersonalSettingEvent) {
        when (event) {
            is PersonalSettingEvent.UpdateNickName -> {
                viewModelScope.launch {
                    if (userUseCase.updateNickName(event.nickName)) {
                        _state.update {
                            it.copy(nickName = event.nickName)
                        }
                    }
                }
            }
            is PersonalSettingEvent.UploadUserAvatar -> {
                viewModelScope.launch {
                    if (userUseCase.uploadUserAvatar(event.file)) {
                        event.selectPicture()
                    }
                }
            }
        }
    }
}