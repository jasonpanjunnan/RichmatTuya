package com.richmat.mytuya.data.posts

import com.tuya.smart.home.sdk.bean.HomeBean
import com.tuya.smart.sdk.bean.DeviceBean
import kotlinx.coroutines.InternalCoroutinesApi
import kotlinx.coroutines.flow.Flow

/**
 * Interface to the Posts data layer.
 * 来捣乱的
 * 为了提高效率，此层的代码，最好放在suspend中执行
 */
interface PostsRepository {

    //获取房间
    @InternalCoroutinesApi
    suspend fun getHomeBeans(): MutableList<HomeBean>

    suspend fun getHomeBean(homeId: Long): HomeBean

    suspend fun andAllDeviceListener(deviceList: List<DeviceBean>)

    suspend fun removeDevice(deviceId: String): Boolean

    fun observeIsShowDialog(): Flow<Boolean>

    //关闭dialog，重置flow的值
    fun resetDeviceMemory(dev: DeviceBean)
}
