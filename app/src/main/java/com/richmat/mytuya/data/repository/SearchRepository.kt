package com.richmat.mytuya.data.repository

import com.tuya.smart.sdk.bean.DeviceBean

interface SearchRepository {

    suspend fun startSearch(devId: String): DeviceBean

    suspend fun getChildDevice(devId: String): List<DeviceBean>

    suspend fun registerDeviceListener(deviceBean: DeviceBean)
}