package com.richmat.mytuya.ui.searchDevice

import android.util.Log
import android.widget.Toast
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountBox
import androidx.compose.material.icons.filled.Highlight
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.richmat.mytuya.MyApplication
import com.richmat.mytuya.R
import com.richmat.mytuya.data.posts.Imp.FakePostsRepository
import com.richmat.mytuya.util.data.DevResultMassage
import com.tuya.smart.sdk.bean.DeviceBean
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

private val kindList: List<Pair<Int, List<DeviceItem>>> = listOf(
    R.string.light to listOf(
        DeviceItem(
            painter = Icons.Default.Highlight,
            name = R.string.zigbee_white_light,
            type = R.string.zigbee,
            needGate = true
        )
    ),
    R.string.gate to listOf(
        DeviceItem(
            painter = Icons.Default.AccountBox,
            name = R.string.wifi_zigbee_gate,
            type = R.string.zigbee
        )
    ),
)

data class DeviceItem(
    val painter: ImageVector,
    val name: Int,
    val type: Int,
    val needGate: Boolean = false,
)

data class SelectDevice(
//    var deviceKindList: List<String> = listOf()
    var deviceKindList: List<Pair<Int, List<DeviceItem>>> = kindList,
    var position: Int = 0,
    val homeId: Long = 1000,
    val zigbeeGateList: List<DeviceBean> = emptyList(),
//    var deviceKindList: List<Map<String,List<String>>> = listOf()
) {
//    var deviceMap:HashMap<String,List<String>> = hashMapOf()

    fun getDeviceList(): List<Int> = deviceKindList.map { it.first }

    fun getSelectDeviceList(position: Int): List<DeviceItem> = deviceKindList[position].second
//    {
//        deviceKindList.forEach {
//            if (it.first ==)
//        }
//    }
}

@HiltViewModel
class SelectDeviceViewModel @Inject constructor(
    savedStateHandle: SavedStateHandle,
    private val postsRepository: FakePostsRepository,
) : ViewModel() {
    private val _uiState = MutableStateFlow(SelectDevice())
    val uiState: StateFlow<SelectDevice> = _uiState

    private val homeId = savedStateHandle.get<Long>(HOME_ID)!!

    fun changeSelectItem(newSelect: Int) {
        _uiState.update { it.copy(position = newSelect) }
    }

    init {
        Toast.makeText(MyApplication.context, homeId.toString(), Toast.LENGTH_SHORT)
            .show()
        _uiState.update {
            it.copy(homeId = homeId)
        }
        viewModelScope.launch {
            val list = postsRepository.getZigbeeGateList()
            _uiState.update { it.copy(zigbeeGateList = list) }
        }
    }

    fun getRoute(deviceId: String): String? = postsRepository.getRouteById(deviceId)

    companion object {
        const val HOME_ID = "home_id"
    }
}