package com.richmat.mytuya.ui.device.gateway

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.richmat.mytuya.data.posts.Imp.FakePostsRepository
import com.richmat.mytuya.data.repository.SearchRepository
import com.tuya.smart.sdk.bean.DeviceBean
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject

data class ChildDeviceData(
    val deviceBean: DeviceBean = DeviceBean(),
    val setupTwo: Boolean = false,
    val setupThree: Boolean = false,
    val success: Boolean = false
) {

}

@HiltViewModel
class ChildSearchViewModel @Inject constructor(
    private val searchRepository: SearchRepository,
    private val fakePostsRepository: FakePostsRepository,
    savedStateHandle: SavedStateHandle
) : ViewModel() {

    private val deviceId: String = savedStateHandle.get<String>(SEARCH_CHILD_KEY)!!

    private val _uiState = MutableStateFlow(ChildDeviceData())
    val uiState: StateFlow<ChildDeviceData> = _uiState

    init {
        viewModelScope.launch {
            try {
                val deviceBean =
                    withContext(Dispatchers.Default) {
                        fakePostsRepository.startSearch(deviceId)
//                        searchRepository.startSearch(deviceId)
                    }
                fakePostsRepository.addDeviceList(device = deviceBean)
                //没必要在这监听，返回home刷新
//                val registerResult =
//                    withContext(Dispatchers.Default) {
//                        searchRepository.registerDeviceListener(
//                            deviceBean = deviceBean
//                        )
//                    }

                _uiState.update {
                    it.copy(deviceBean = deviceBean, success = true)
                }
            } catch (e: Exception) {
                println(e)
                e.printStackTrace()
            }
        }
    }

    companion object {
        const val SEARCH_CHILD_KEY = "searchChildId"

//        fun provideFactory(
//            searchRepository: SearchRepository,
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
//                    return ChildSearchViewModel(searchRepository, handle) as T
//                }
//            }
    }
}