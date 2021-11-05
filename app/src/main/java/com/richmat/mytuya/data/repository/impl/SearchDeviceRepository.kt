package com.richmat.mytuya.data.repository.impl

import com.richmat.mytuya.data.repository.SearchRepository
import com.tuya.smart.home.sdk.TuyaHomeSdk
import com.tuya.smart.sdk.api.ITuyaDataCallback
import com.tuya.smart.sdk.bean.DeviceBean
import javax.inject.Inject
import kotlin.coroutines.resume
import kotlin.coroutines.resumeWithException
import kotlin.coroutines.suspendCoroutine


class SearchDeviceRepository @Inject constructor(): SearchRepository {

    //Defalt
    override suspend fun startSearch(devId: String) = DeviceBean()

    override suspend fun registerDeviceListener(deviceBean: DeviceBean) {}

    override suspend fun getChildDevice(devId: String): List<DeviceBean> {
        return suspendCoroutine { continuation ->
            TuyaHomeSdk.newGatewayInstance(devId)
                .getSubDevList(object : ITuyaDataCallback<List<DeviceBean>> {
                    override fun onSuccess(list: List<DeviceBean>) {
                        continuation.resume(list)
                    }

                    override fun onError(errorCode: String, errorMessage: String) {
                        continuation.resumeWithException(Exception("$errorCode, $errorMessage"))
                    }
                })
        }
    }

}