package com.richmat.mytuya.ui.newHome.homeViewmoel

import android.util.Log
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.alibaba.fastjson.JSONObject
import com.richmat.mytuya.data.posts.Imp.FakePostsRepository
import com.richmat.mytuya.ui.newHome.homeViewmoel.viewmodel.Post
import com.richmat.mytuya.util.data.TAG
import com.tuya.smart.android.base.bean.CountryRespBean
import com.tuya.smart.home.sdk.bean.HomeBean
import com.tuya.smart.sdk.bean.DeviceBean
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.InternalCoroutinesApi
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.time.LocalDateTime
import java.util.*
import javax.inject.Inject

data class Delete(
    val post: Post? = null,
    val isFavorite: Boolean = false,
    val loading: Boolean = false,
    val homeBeans: List<HomeBean> = mutableListOf(),
    val homeId: Long = 1000L,
    val bean: HomeBean = HomeBean(),
    val isShowHint: Boolean = false,
    val remove: Boolean = false,
    val eventList: List<String> = listOf("人体感应器", "红外报警", "Rich", LocalDateTime.now().toString()),
    val removeSet: Set<String> = setOf(),
    val homeNumber: Int = 0,
    val deviceList: List<DeviceBean> = emptyList(),
    val countries: List<String> = emptyList(),
) {
    val failedLoading: Boolean
        get() = !loading && post == null
}

@HiltViewModel
@InternalCoroutinesApi
class HomeViewModel @Inject constructor(
    private val postsRepository: FakePostsRepository,
    savedStateHandle: SavedStateHandle,
) : ViewModel() {

    //TODO 根据navhost id 获取信息， 那还要factory干嘛呢？？ 9.22注：不传值不行
//    private val postId: String = savedStateHandle.get<String>(DELETE)!!

    private val _uiState = MutableStateFlow(Delete(loading = true))
    val uiState: StateFlow<Delete> = _uiState

    init {
        Log.e(TAG, "home初始化: ")
        refreshHome()
        // 第二次注意，一个viewModelScope只能观察一个
        viewModelScope.launch {
            launch {
                postsRepository.observeRemoveSet().collect { removeSet ->
                    _uiState.update { it.copy(removeSet = removeSet) }
                }
            }
            launch {
                postsRepository.observeHomeBeans().collect { homes ->
                    _uiState.update { it.copy(homeBeans = homes) }
                }
            }

            launch {
                postsRepository.observeDeviceList().collect { devices ->
                    Log.e(TAG, "observeDeviceList: ${devices.size}")
                    _uiState.update { it.copy(deviceList = devices) }
                }
            }
        }
//        getHomeBean(_uiState.value.homeId)
    }

    //TODO 下拉刷新
    fun refreshHome() {
        Log.e(TAG, "refreshHome: 下拉刷新")
        _uiState.update { it.copy(loading = true) }
        viewModelScope.launch {
            try {
                val homeBeans =
                    withContext(Dispatchers.IO) { postsRepository.getHomeBeans() }
                val homeBean =
                    withContext(Dispatchers.IO) { postsRepository.getHomeBean(homeId = homeBeans[uiState.value.homeNumber].homeId) }
                //给所有设备添加监听
                postsRepository.andAllDeviceListener(homeBean.deviceList)

//            val homeBeans = postsRepository.getHomeBeans()
                _uiState.update {
                    //默认是拿的第一个家庭，课改
                    it.copy(
                        homeBeans = homeBeans,
                        homeId = homeBeans[0].homeId,
                        bean = homeBean,
                        loading = false
//                        deviceList = homeBean.deviceList
                    )
                }
            } catch (e: Exception) {
                _uiState.update { it.copy(loading = false) }
                println(e)
            }
        }

        viewModelScope.launch {
            postsRepository.observeIsShowDialog().collect { bln ->
                _uiState.update { it.copy(isShowHint = bln) }
            }
        }
    }

    fun newRefreshHome() {
        viewModelScope.launch {
            try {
                withContext(Dispatchers.IO) {
                    postsRepository.getHomeBeans()
                }
            } catch (e: Exception) {
                println(e)
            }
        }
    }

    fun getHomeBean(homeId: Long) {
        viewModelScope.launch {
            val homeBean =
                withContext(Dispatchers.IO) { postsRepository.getHomeBean(homeId = homeId) }
            _uiState.update {
                it.copy(bean = homeBean)
            }
            Log.e(TAG, "getHomeBean: $homeId,  ${_uiState.value.homeId}")
        }
    }

    fun closeDialog(device: DeviceBean) {
        postsRepository.resetDeviceMemory(device)
    }

    fun removeDevice(deviceId: String) {
        viewModelScope.launch {
            try {
                val remove = withContext(Dispatchers.IO) {
                    postsRepository.removeDevice(deviceId = deviceId)
                }
                _uiState.update {
                    it.copy(remove = remove)
                }
            } catch (e: Exception) {
                println(e)
            }
        }
    }

    fun addOrRemove(deviceId: String) {
        viewModelScope.launch {
            postsRepository.selectRemoveDevice(deviceId = deviceId)
        }
    }

    fun removeDeviceSet() {
        viewModelScope.launch {
            postsRepository.removeDeviceSet()
        }
    }

    /**
     * 获取route。此处可能有机会优化
     */
    fun getRoute(deviceId: String): String? = postsRepository.getRouteById(deviceId)

    companion object {
        const val DELETE = "delete"

        //时代的眼泪
//        fun provideFactory(
//            postsRepository: FakePostsRepository,
//            owner: SavedStateRegistryOwner,
//            deaultArgs: Bundle?,
//        ): AbstractSavedStateViewModelFactory =
//            object : AbstractSavedStateViewModelFactory(owner, deaultArgs) {
//                @Suppress("UNCHECKED_CAST")
//                override fun <T : ViewModel?> create(
//                    key: String,
//                    modelClass: Class<T>,
//                    handle: SavedStateHandle
//                ): T {
//                    return HomeViewModel(postsRepository, handle) as T
//                }
//            }
    }
}
