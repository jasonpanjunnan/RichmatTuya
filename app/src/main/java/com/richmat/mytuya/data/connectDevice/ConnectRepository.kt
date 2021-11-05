package com.richmat.mytuya.data.connectDevice

import android.content.Context
import android.util.Log
import com.richmat.mytuya.ui.searchDevice.ConnectStep
import com.richmat.mytuya.ui.searchDevice.DEVICE_SEARCH_FINISH
import com.richmat.mytuya.ui.searchDevice.SearchStep
import com.richmat.mytuya.util.data.TAG
import com.tuya.smart.home.sdk.TuyaHomeSdk
import com.tuya.smart.home.sdk.builder.ActivatorBuilder
import com.tuya.smart.sdk.api.ITuyaActivator
import com.tuya.smart.sdk.api.ITuyaActivatorGetToken
import com.tuya.smart.sdk.api.ITuyaSmartActivatorListener
import com.tuya.smart.sdk.bean.DeviceBean
import com.tuya.smart.sdk.enums.ActivatorModelEnum
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import javax.inject.Inject
import kotlin.coroutines.resume
import kotlin.coroutines.resumeWithException
import kotlin.coroutines.suspendCoroutine


class ConnectRepository @Inject constructor() {

    lateinit var builder: ActivatorBuilder
    lateinit var mTuyaActivator: ITuyaActivator

    private val stepFlow = MutableStateFlow<ConnectStep>(ConnectStep.ConnectFirst(step = ""))
    private val searchResult = MutableStateFlow(false)

    suspend fun request(homeId: Long): String {
        return suspendCoroutine { continuation ->
            TuyaHomeSdk.getActivatorInstance()
                .getActivatorToken(homeId,
                    object : ITuyaActivatorGetToken {
                        override fun onSuccess(token: String) {
//                            Home.token = token
                            Log.e(TAG, "onSuccess: $token")
                            continuation.resume(token)
                        }

                        override fun onFailure(errorCode: String?, errorMsg: String?) {
                            Log.e(TAG, "onFailure: $errorCode, $errorMsg")
                            //TODO 先这样写把
                            continuation.resumeWithException(Exception(errorCode))
                        }
                    })
        }
    }

    fun stopSearch() {
        if (::mTuyaActivator.isInitialized) {
            mTuyaActivator.stop()
            mTuyaActivator.onDestroy()
        }
    }

    suspend fun searchGateway(
        context: Context,
        wifiName: String,
        wifiPassword: String,
        timeOut: Long,
        token: String
    ): DeviceBean {
        return suspendCoroutine { continuation ->
            builder = ActivatorBuilder()
                .setContext(context)
                .setSsid(wifiName)
                .setPassword(wifiPassword)
                .setActivatorModel(ActivatorModelEnum.TY_AP)
                .setTimeOut(timeOut)
                .setToken(token)
                .setListener(object : ITuyaSmartActivatorListener {
                    override fun onError(errorCode: String?, errorMsg: String?) {
                        Log.e(TAG, "onError: $errorCode, $errorMsg")
                        continuation.resumeWithException(Exception("$errorCode, $errorMsg"))
                    }

                    override fun onActiveSuccess(devResp: DeviceBean) {
                        stepFlow.value = SearchStep(DEVICE_SEARCH_FINISH, "").getConnectStep()
//                        Home.deviceBean = devResp
                        val dps =
                            TuyaHomeSdk.getDataInstance().getDps(devResp.getDevId())
                        var details: String = ""
                        dps?.forEach {
                            details += "${it.key} + \"\"+ ${it.value}"
                        }
                        Log.e(
                            TAG,
                            "ConnectRepository onActiveSuccess: ${devResp.name}: $details, ${devResp.devId}"
                        )
                        continuation.resume(devResp)
                    }

                    override fun onStep(step: String, data: Any?) {
                        searchResult.value = true
                        stepFlow.value = SearchStep(step, data).getConnectStep()
                        Log.e(TAG, "onStep: $step, $data")
                    }
                })
            mTuyaActivator =
                TuyaHomeSdk.getActivatorInstance().newActivator(builder)
            mTuyaActivator.start()
        }
    }

    fun observeConnectStep(): Flow<ConnectStep> = stepFlow

    fun observeSearchResult(): Flow<Boolean> = searchResult
}