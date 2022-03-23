package com.richmat.mytuya.ui.deviceSetting

import android.widget.Toast
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.richmat.mytuya.MyApplication
import com.richmat.mytuya.data.posts.Imp.FakePostsRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject

data class DevSettingInfo(
    val iconUri: String,
    val devName: String,
    val deviceId: String,
//    val devMessage: DevResultMassage,
) {}

@HiltViewModel
class DevSettingViewModel @Inject constructor(
    savedStateHandle: SavedStateHandle,
    private val myRepository: FakePostsRepository,
) : ViewModel() {
    private val devInfo = savedStateHandle.get<String>(DEV_INFO)!!

    private val _uiState =
        MutableStateFlow(DevSettingInfo(iconUri = "", "未命名", "1000"))
    val uiState: StateFlow<DevSettingInfo> = _uiState

    fun changeName(newName: String) {
        _uiState.update { it.copy(devName = newName) }
    }

    fun rename() {
        viewModelScope.launch {
            var result: Boolean
            try {
                result = withContext(Dispatchers.IO) {
                    myRepository.rename(_uiState.value.deviceId, _uiState.value.devName)
                }
            } catch (e: Exception) {
                result = false
//                throw e
                println(e)
            }
            val toast = if (result) "修改名字成功" else "修改名字失败"
            Toast.makeText(MyApplication.context, toast, Toast.LENGTH_SHORT).show()
        }
    }

    init {
        myRepository.currentDevBean?.let {
            val devBean = it
            _uiState.update {
                it.copy(devName = devBean.name,
                    iconUri = devBean.iconUrl,
                    deviceId = devBean.devId)
            }
        }
    }

    companion object {
        const val DEV_INFO = "dev_info"
    }
}