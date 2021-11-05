package com.richmat.mytuya.ui.searchDevice

import android.util.Log
import android.widget.Toast
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.datastore.core.DataStore
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.richmat.mytuya.MyApplication
import com.richmat.mytuya.UserPreferences
import com.richmat.mytuya.data.connectDevice.ConnectRepository
import com.richmat.mytuya.data.posts.Imp.FakePostsRepository
import com.richmat.mytuya.ui.searchDevice.SelectDeviceViewModel.Companion.HOME_ID
import com.richmat.mytuya.util.data.DevResultMassage
import com.richmat.mytuya.util.data.TAG
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import javax.inject.Inject

data class SearchUiState(
    val token: String = "",
    val searchState: List<SearchSurfaceState> = listOf(),
    val isShowWarning: Boolean = false,
    val homeId: Long = 1000,
    var searchStep: ConnectStep = ConnectStep.ConnectFirst(step = ""),
    var searchFinish: SearchResult? = null,
//    val searchDevName: String = "未命名",
//    val route: String? = "",
    val devResultMassage: DevResultMassage,
) {
    //获取到token才能继续
    val isSuccessGetToken: Boolean
        get() = token.isNotEmpty()
    var currentQuestionIndex by mutableStateOf(0)
}

@HiltViewModel
class SearchDeviceViewModel @Inject constructor(
    private val repository: ConnectRepository,
    savedStateHandle: SavedStateHandle,
    private val myRepository: FakePostsRepository,
    private val userPreferences: DataStore<UserPreferences>,
) : ViewModel() {
    private val homeId: Long = savedStateHandle.get<Long>(HOME_ID)!!
//    private val location: String = savedStateHandle.get<String>(LOCATION_NAME)!!

    private val _uiState =
        MutableStateFlow(SearchUiState(devResultMassage = DevResultMassage("", "")))
    val uiState: StateFlow<SearchUiState> = _uiState

    /**
     * 把wifi的账号和密码，提出来. 好用，暂留，把SearchUiState里边的删了，暂时没问题
     */
    private val _wifiState = MutableStateFlow(Pair("", ""))
    val wifiState: StateFlow<Pair<String, String>> = _wifiState

    fun onChangeName(newName: String) {
        _wifiState.value = Pair(newName, _wifiState.value.second)
    }

    fun onChangePassword(newPassword: String) {
        _wifiState.value = Pair(_wifiState.value.first, newPassword)
    }

    val searchResult = repository.observeSearchResult().stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5000),
        initialValue = false
    )

    val searchStep = repository.observeConnectStep().stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5000),
        initialValue = ConnectStep.ConnectFirst(step = "")
    )

    init {
        val searchs: List<SearchSurfaceState> = searchDetails.mapIndexed { index, searchDetail ->
            val showPrevious = index > 0
            val showDone = index == searchDetails.size - 1
            SearchSurfaceState(
                searchDetail = searchDetail,
                showDone = showDone,
                showPrevious = showPrevious,
                searchIndex = index
            )
        }

        _uiState.update { it.copy(searchState = searchs, homeId = homeId) }
        viewModelScope.launch {
            //TODO 从这获取wifi名称
            val wifiName = userPreferences.data.firstOrNull()?.wifiName
            val wifiPassword = userPreferences.data.firstOrNull()?.wifiPassword

            if (wifiName != null && wifiPassword != null) {
                _wifiState.value = Pair(wifiName, wifiPassword)
            }
//            myRepository.observeIsShowDialog().collect { bln ->
//                Log.e(TAG, "SearchDeviceViewModel, observeIsShowDialog: $bln")
//                _uiState.update { it.copy(isShowWarning = bln) }
//            }
        }
    }

    fun startSearch() {
        viewModelScope.launch {
//            try {
//                val deviceBean = withContext(IO) {
//                    repository.searchGateway(
//                        MyApplication.context,
//                        wifiName = _uiState.value.wifiName,
//                        wifiPassword = _uiState.value.wifiPassword,
//                        timeOut = 100L,
//                        token = _uiState.value.token
//                    )
//                }
//
//                myRepository.addDeviceList(deviceBean)
//            } catch (e: Exception) {
//                println(" 错误错误$e")
//            }
            try {
                val device = repository.searchGateway(
                    MyApplication.context,
                    wifiName = _wifiState.value.first,
                    wifiPassword = _wifiState.value.second,
                    timeOut = 100L,
                    token = _uiState.value.token
                )
                myRepository.addDeviceList(device)
                val deviceListener = myRepository.getDeviceListener(devId = device.devId)
                val registerResult =
                    myRepository.registerDevListener(deviceBean = device) { deviceListener }
                Log.e(
                    TAG,
                    "startSearch: sssssssss ${deviceListener?.route}, ${deviceListener == null}",
                )
                if (registerResult) {
                    _uiState.update {
                        it.copy(
                            searchFinish = SearchResult.Success,
//                            route = deviceListener?.route,
//                            searchDevName = device.name,
                            devResultMassage = DevResultMassage(name = device.name,
                                route = deviceListener?.route!!, deviceId = device.devId))

                    }
                } else {
                    Toast.makeText(MyApplication.context, "暂不支持该设备", Toast.LENGTH_SHORT).show()
                }
            } catch (e: Exception) {
                println(e)
            }

        }
    }

    fun getToken() {
        viewModelScope.launch {
            try {

                val token = repository.request(uiState.value.homeId)
                _uiState.update { it.copy(token = token) }
                //下一页
                _uiState.value.currentQuestionIndex++
            } catch (e: Exception) {
                println("错误$e")
            }
        }
    }

    fun stopSearch() {
        repository.stopSearch()
    }

    fun rememberAccount(wifiPassword: String, wifiName: String) {
        viewModelScope.launch {
            userPreferences.updateData {
                it.toBuilder().setWifiName(wifiName).setWifiPassword(wifiPassword).build()
            }
        }
        _wifiState.value = Pair(wifiName, wifiPassword)
    }

    fun changeResult(searchFinish: SearchResult?) {
        _uiState.update { it.copy(searchFinish = searchFinish) }
    }

    companion object {
    }
}