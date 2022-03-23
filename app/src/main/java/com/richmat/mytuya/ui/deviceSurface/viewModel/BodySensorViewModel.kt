package com.richmat.mytuya.ui.deviceSurface.viewModel

import android.util.Log
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.richmat.mytuya.data.posts.Imp.FakePostsRepository
import com.richmat.mytuya.util.data.TAG
import com.tuya.smart.sdk.bean.DeviceBean
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

data class BodySensorResponse(
    val deviceBean: DeviceBean = DeviceBean(),
    val warningsList: List<String> = emptyList(),
    val isShowWarning: Boolean = false,
) {

}

@HiltViewModel
class BodySensorViewModel @Inject constructor(
    private val postsRepository: FakePostsRepository,
    savedStateHandle: SavedStateHandle,
) : ViewModel() {

    private val deviceId: String = savedStateHandle.get<String>(DEVICE_ID)!!

    private val _uiState = MutableStateFlow(BodySensorResponse())
    val uiState: StateFlow<BodySensorResponse> = _uiState

    init {

        Log.e(TAG, "ssssss: $deviceId")
        viewModelScope.launch {
            //必须初始化
//            deviceManager.initializeDevice(deviceId)

            postsRepository.observeWarningList().collect { list ->
                Log.e(TAG, "BodySensorViewModel  ${list.size}: ")
                _uiState.update { it.copy(warningsList = list) }
            }
        }
        //大大的？？？，viewModelScope这个协程有问题.可能是因为一个协程只能处理一个collect 或者 update
        viewModelScope.launch {
            postsRepository.observeIsShowDialog().collect { bln ->
                Log.e(TAG, "2222222222222: $bln")
                _uiState.update { it.copy(isShowWarning = bln) }
            }
        }
    }

    fun removeDevice() {
        viewModelScope.launch {
//            val isSuccess = withContext(Dispatchers.IO) { deviceManager.removeDevice() }
//            if (isSuccess) {
//                Toast.makeText(MyApplication.context, " 移除设备成功", Toast.LENGTH_SHORT).show()
//            }
        }
    }

    fun removeListener() {
//        deviceManager.removeListener()
//        Toast.makeText(MyApplication.context, " 取消监听成功", Toast.LENGTH_SHORT).show()
    }

    fun resetWarning() {
        viewModelScope.launch {
            delay(6000)
            postsRepository.resetDeviceMemory(DeviceBean())
        }
    }

    companion object {
        const val DEVICE_ID = "deviceId"

//        fun provideFactory(
//            owner: SavedStateRegistryOwner,
//            default: Bundle?
//        ): AbstractSavedStateViewModelFactory =
//            object : AbstractSavedStateViewModelFactory(owner, default) {
//                @Suppress("UNCHECKED_CAST")
//                override fun <T : ViewModel?> create(
//                    key: String,
//                    modelClass: Class<T>,
//                    handle: SavedStateHandle
//                ): T {
//                    return BodySensorViewModel(savedStateHandle = handle) as T
//                }
//            }
    }
}