package com.richmat.mytuya.ui.device.gateway

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.richmat.mytuya.data.posts.Imp.FakePostsRepository
import com.richmat.mytuya.data.repository.SearchRepository
import com.richmat.mytuya.util.data.ZIGBEE_GATE
import com.tuya.smart.sdk.bean.DeviceBean
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject

data class GatewayData(
    val deviceBeans: List<DeviceBean> = listOf(),
    val devMessage: DeviceBean? = null,
) {

}

@HiltViewModel
class GatewayViewModel @Inject constructor(
    private val searchRepository: SearchRepository,
    savedStateHandle: SavedStateHandle,
    private val postsRepository: FakePostsRepository,
) : ViewModel() {

    val deviceId: String = ZIGBEE_GATE
    private val devInfo: String = savedStateHandle.get<String>(GATEWAY_CHILD_KEY)!!

    private val _uiState = MutableStateFlow(GatewayData())
    val uiState: StateFlow<GatewayData> = _uiState

    init {
        val devBean = postsRepository.getDevBeanById(deviceId)
        postsRepository.currentDevBean = devBean
        _uiState.update { it.copy(devMessage = devBean) }
        viewModelScope.launch {
            try {
                val deviceBeans = withContext(Dispatchers.IO) {
                    searchRepository.getChildDevice(deviceId)
                }
                _uiState.update {
                    it.copy(deviceBeans = deviceBeans)
                }
            } catch (e: Exception) {
                println(e)
//                throw e
            }
        }
    }

    fun removeDevice() {
        viewModelScope.launch {
            postsRepository.removeDevice(deviceId)
        }
    }

    companion object {
        const val GATEWAY_CHILD_KEY = "searchChildId"
    }
}