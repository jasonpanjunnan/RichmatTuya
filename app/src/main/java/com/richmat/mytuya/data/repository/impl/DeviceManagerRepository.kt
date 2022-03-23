package com.richmat.mytuya.data.repository.impl

import com.tuya.smart.home.sdk.TuyaHomeSdk
import com.tuya.smart.sdk.api.IResultCallback
import com.tuya.smart.sdk.api.ITuyaDevice
import kotlin.coroutines.resume
import kotlin.coroutines.resumeWithException
import kotlin.coroutines.suspendCoroutine

class DeviceManagerRepository
// TODO @Inject constructor(),不需要了
{
    private var mDevice: ITuyaDevice? = null
    suspend fun removeDevice(): Boolean {
        return suspendCoroutine { continuation ->
            mDevice?.removeDevice(object : IResultCallback {
                override fun onError(code: String?, error: String?) {
                    continuation.resumeWithException(Exception("$code, $error"))
                }

                override fun onSuccess() {
                    continuation.resume(true)
                }
            })
        }
    }

    fun removeListener() {
        mDevice?.unRegisterDevListener()
    }

    fun initializeDevice(deviceId: String) {
        mDevice = TuyaHomeSdk.newDeviceInstance(deviceId)
    }

    suspend fun resetDevice(): Boolean {
        return suspendCoroutine { continuation ->
            mDevice?.resetFactory(object : IResultCallback {
                override fun onError(code: String?, error: String?) {
                    continuation.resumeWithException(Exception("$code, $error"))
                }

                override fun onSuccess() {
                    continuation.resume(true)
                }
            })
        }
    }
}