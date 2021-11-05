package com.richmat.mytuya.data.posts.Imp

import android.util.Log
import android.widget.Toast
import com.alibaba.fastjson.JSON
import com.alibaba.fastjson.JSONObject
import com.richmat.mytuya.MyApplication
import com.richmat.mytuya.data.posts.PostsRepository
import com.richmat.mytuya.util.addOrRemove
import com.richmat.mytuya.util.data.DevResultMassage
import com.richmat.mytuya.util.data.DeviceListener
import com.richmat.mytuya.util.data.TAG
import com.richmat.mytuya.util.getDsp
import com.tuya.smart.home.sdk.TuyaHomeSdk
import com.tuya.smart.home.sdk.bean.HomeBean
import com.tuya.smart.home.sdk.builder.TuyaGwSubDevActivatorBuilder
import com.tuya.smart.home.sdk.callback.ITuyaGetHomeListCallback
import com.tuya.smart.home.sdk.callback.ITuyaHomeResultCallback
import com.tuya.smart.sdk.api.IDevListener
import com.tuya.smart.sdk.api.IResultCallback
import com.tuya.smart.sdk.api.ITuyaActivator
import com.tuya.smart.sdk.api.ITuyaSmartActivatorListener
import com.tuya.smart.sdk.bean.DeviceBean
import kotlinx.coroutines.InternalCoroutinesApi
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.sync.Mutex
import kotlinx.coroutines.sync.withLock
import javax.inject.Inject
import javax.inject.Singleton
import kotlin.coroutines.resume
import kotlin.coroutines.resumeWithException
import kotlin.coroutines.suspendCoroutine

//val myFakePostsRepository = FakePostsRepository()

//TODO hilt好像不能身兼多职，比如一边接口，，一边实体；最新 ：可以的 是hilt不能by导致的
//
//TODO 将结果放入Result中
@Singleton
class FakePostsRepository @Inject constructor() : PostsRepository {

    private val isShowDialog = MutableStateFlow(false)
    private val warningList = MutableStateFlow(mutableListOf<String>())

    var currentDevBean: DeviceBean? = null
//        private set

    //储存的是要删除的DeviceId
    private val removeSet = MutableStateFlow(mutableSetOf<String>())

    //没必要，难以管理，从deviceList中获取，比较好
    private val zigbeeGateSet = MutableStateFlow(mutableSetOf<DevResultMassage>())

    private val homeBeans = MutableStateFlow(listOf<HomeBean>())
    private val deviceList = MutableStateFlow(listOf<DeviceBean>())

    private var registerSet: MutableSet<String> = mutableSetOf()

//    private val zigbeeGateSet: MutableSet<DevResultMassage> = mutableSetOf()


    // Used to make suspend functions that read and update state safe to call from any thread
    private val mutex = Mutex()

    suspend fun selectRemoveDevice(deviceId: String) {
        mutex.withLock {
            val set = removeSet.value.toMutableSet()
            set.addOrRemove(deviceId)
            removeSet.value = set
        }
    }

    @InternalCoroutinesApi
    override suspend fun getHomeBeans(): MutableList<HomeBean> {
        return suspendCoroutine { continuation ->
            TuyaHomeSdk.getHomeManagerInstance().queryHomeList(object : ITuyaGetHomeListCallback {
                override fun onSuccess(homeBeans: MutableList<HomeBean>) {
//                    homeList = homeBeans as ArrayList<HomeBean>
//                if (homeList.size > 0) {
//                    Home.currentHomeBean = homeList[0]
                    this@FakePostsRepository.homeBeans.value = homeBeans
                    Log.e(TAG, "onSuccess:222   :${this@FakePostsRepository.homeBeans.value}")
                    continuation.resume(homeBeans)
//                }
                }

                override fun onError(errorCode: String?, error: String?) {
                    continuation.resumeWithException(Exception("$errorCode, $error"))
                    Toast.makeText(MyApplication.context, "没有发现家庭，请创建！", Toast.LENGTH_SHORT).show()
                    Log.e(TAG, "onError: 列表为空")
                }
            })
        }
    }

    override suspend fun getHomeBean(homeId: Long): HomeBean {
        return suspendCoroutine { continuation ->
            //初始化设备。app存活期间，执行一次
            TuyaHomeSdk.newHomeInstance(homeId)
                .getHomeDetail(object : ITuyaHomeResultCallback {
                    override fun onSuccess(bean: HomeBean?) {
//                        Home.currentHomeBean = bean
                        if (bean != null) {
                            this@FakePostsRepository.deviceList.value = bean.deviceList
//                                    viewModel.changeHomeBean(bean)
                            continuation.resume(bean)
                        }
                    }

                    override fun onError(errorCode: String?, errorMsg: String?) {
                        continuation.resumeWithException(Exception("$errorCode ，￥$errorMsg"))
                        Log.e(TAG, "onError: 获取失败, $errorMsg")
                    }
                })
        }
    }

    //TODO 可能是最复杂的功能，不知道咋分类
    override suspend fun andAllDeviceListener(deviceList: List<DeviceBean>) {
        deviceList.forEach { deviceBean ->
            registerDevListener(deviceBean = deviceBean) {
                getDeviceListener(it)
            }
            //网关不需要监听，直接监听设备
//            val devId = deviceBean.devId
//            if (registerSet.contains(devId) && !deviceBean.isZigBeeWifi) {
//                Log.e(TAG, "andAllDeviceListener: ${devId}")
//                val deviceListener = getDeviceListener(devId)
//                if (deviceListener == null) {
//                    Toast.makeText(MyApplication.context, "设备界面不存在", Toast.LENGTH_SHORT).show()
//                    return
//                }
//                val device = TuyaHomeSdk.newDeviceInstance(devId)
//                val deviceParse = getDeviceParse(devId, deviceListener)
//                device.registerDevListener(deviceParse)
//                registerSet.add(devId)
//            }
//                object : IDevListener {
//                    //pir代表有人经过
//                    override fun onDpUpdate(devId: String?, dpStr: String) {
//                        if (dpStr.contains("pir")) {
////                        isShowDialog.value = true
//                            Toast.makeText(
//                                MyApplication.context,
//                                "有人经过11",
//                                Toast.LENGTH_SHORT
//                            ).show()
//                            Log.e(
//                                TAG,
//                                "onDpUpdate pir: isShowDialog：$isShowDialog, 来自于 ￥${deviceBean.category}，s设备名称：${deviceBean.name}"
//                            )
//                            if (!deviceBean.isZigBeeWifi) {
//                                //TODO Room保存，待写
//
//                            }
//                        }
//
//                        if (!deviceBean.isZigBeeWifi) {
//                            //TODO Room保存，待写
//                            val list = warningList.value.toMutableList()
//                            list.add("\"onDpUpdate: devId :${deviceBean.name}, dpstr: $dpStr\"")
//                            warningList.value = list
//                        }
//                    }
//
//                    override fun onRemoved(devId: String?) {
//                        Log.e(TAG, "onRemoved: ")
//                        updateWarningList(deviceBean = deviceBean, "onRemoved: devId :$devId")
//                    }
//
//                    override fun onStatusChanged(devId: String?, online: Boolean) {
//                        Log.e(TAG, "FakePostsRepository onStatusChanged: ")
//                        updateWarningList(
//                            deviceBean = deviceBean,
//                            "onStatusChanged: devId name:${deviceBean.name}, online: $online"
//                        )
//                    }
//
//                    override fun onNetworkStatusChanged(
//                        devId: String?,
//                        status: Boolean,
//                    ) {
//                        Log.e(TAG, "onNetworkStatusChanged: ")
//                        updateWarningList(
//                            deviceBean = deviceBean,
//                            "onNetworkStatusChanged: devId :${deviceBean.name}, status: $status"
//                        )
//                    }
//
//                    override fun onDevInfoUpdate(devId: String?) {
//                        Log.e(TAG, "onDevInfoUpdate: ")
//                        updateWarningList(deviceBean = deviceBean, "onDevInfoUpdate: devId :$devId")
//                    }
//                }
//                )
//            }
        }
    }

    fun registerDevListener(
        deviceBean: DeviceBean,
        getListener: (String) -> DeviceListener?,
    ): Boolean {
        Log.e(
            TAG,
            "registerDevListener: deviceBean.devId :${deviceBean.devId},registerSet size: $registerSet",
        )
        val devId = deviceBean.devId
        val deviceListener = getListener(devId)
        if (deviceListener == null) {
            Toast.makeText(MyApplication.context, "设备界面不存在", Toast.LENGTH_SHORT).show()
            return false
        }
        if (deviceBean.isZigBeeWifi) {
            val set = zigbeeGateSet.value.toMutableSet()
            set.add(DevResultMassage(name = deviceBean.name,
                route = deviceListener.route, deviceId = devId))
            zigbeeGateSet.value = set
            Log.e(TAG, "registerDevListener: isZigBeeWifi is ${deviceBean.isZigBeeWifi}")
            return true
        }

        if (!registerSet.contains(devId)) {
            Log.e(TAG, "andAllDeviceListener: ${devId}")

            val device = TuyaHomeSdk.newDeviceInstance(devId)
            val deviceParse = getDeviceParse(devId, deviceListener)
            device.registerDevListener(deviceParse)
            registerSet.add(devId)
        }
        return true
    }

    /**
     * 设备的监听器
     */
    private fun getDeviceParse(deviceId: String, deviceListener: DeviceListener): IDevListener {

        val processMode = updateSomeThing(deviceListener)
        val listener = object : IDevListener {
            override fun onDpUpdate(devId: String, dpStr: String?) {
                if (devId == deviceId) {
                    val json = JSONObject.parseObject(dpStr)
                    //可以用，有更好的方式再说
                    val dpMap = JSON.toJavaObject(json, Map::class.java)
                    Log.e(TAG,
                        "onDpUpdate: dpstr : $dpStr, json,dpMap: $dpMap, ${dpMap["1"]}, ${dpMap["4"]}，是否有人经过：${dpMap["1"] == "pir"}")

                    processMode.onDpUpdate(dpMap)
                }
            }

            override fun onRemoved(devId: String?) {

            }

            override fun onStatusChanged(devId: String?, online: Boolean) {
                processMode.onStatusChanged?.let { it(online) }
            }

            override fun onNetworkStatusChanged(devId: String?, status: Boolean) {

            }

            override fun onDevInfoUpdate(devId: String) {
                //更改名称，在这刷新
                TuyaHomeSdk.getDataInstance().getDeviceBean(devId)
            }

        }
        return listener
    }

    data class ProcessMode(
        val onDpUpdate: (Map<*, *>) -> Unit,
        val onRemoved: (() -> Unit)? = null,
        val onStatusChanged: ((Boolean) -> Unit)? = null,
        val onNetworkStatusChanged: ((Boolean) -> Unit)? = null,
        val onDevInfoUpdate: (() -> Unit)? = null,
    )

    /**
     * 监听功能点的上报
     */
    private fun updateSomeThing(deviceListener: DeviceListener): ProcessMode {
        when (deviceListener) {
            is DeviceListener.BodySensorListener -> {
                return ProcessMode(
                    onDpUpdate = { dpMap ->
                        if (dpMap["1"] == "pir" && dpMap.size == 1) {
                            isShowDialog.value = true
                            val list = warningList.value.toMutableList()
                            list.add("\"onDpUpdate: devId :${deviceListener.deviceId}, dpstr: ${dpMap["1"]} ,map: $dpMap\"")
                            warningList.value = list
                        }
                    }
                )
            }
            DeviceListener.BulbLightListener -> {
                Log.e(TAG, "updateSomeThing: 添加小灯监听")
                return ProcessMode(
                    onDpUpdate = { dpMap ->

                    }
                )
            }
            DeviceListener.GateListener -> TODO()
        }
    }

    /**
     *根据deviceId返回对应设备，
     *Grid的route也来自这
     */
    fun getDeviceListener(devId: String): DeviceListener? = when (devId) {
        DeviceListener.BodySensorListener.deviceId -> {
            DeviceListener.BodySensorListener
        }

        DeviceListener.GateListener.deviceId -> DeviceListener.GateListener

        DeviceListener.BulbLightListener.deviceId -> DeviceListener.BulbLightListener
//        else -> DeviceListener.BodySensorListener
        else -> null
    }

    fun getRouteById(devId: String): String? = getDeviceListener(devId)?.route

    override suspend fun removeDevice(deviceId: String): Boolean {
        val set = zigbeeGateSet.value.toMutableSet()
        set.forEach {
            if (it.deviceId == deviceId) {
                set.remove(it)
                return@forEach
            }
        }
        zigbeeGateSet.value = set
        return suspendCoroutine { continuation ->
            val mDevice = TuyaHomeSdk.newDeviceInstance(deviceId)
            mDevice.removeDevice(object : IResultCallback {
                override fun onError(code: String?, error: String?) {
                    continuation.resumeWithException(Exception("$code, $error"))
                }

                override fun onSuccess() {
                    continuation.resume(true)
                }
            })
        }
    }

    override fun observeIsShowDialog(): Flow<Boolean> = isShowDialog

    fun observeRemoveSet(): Flow<Set<String>> = removeSet

    fun observeZigbeeGateSet(): Flow<Set<DevResultMassage>> = zigbeeGateSet

    fun observeWarningList(): Flow<List<String>> = warningList

    fun observeHomeBeans(): Flow<List<HomeBean>> = homeBeans

    fun observeDeviceList(): Flow<List<DeviceBean>> = deviceList


    //TODO 设备多了，可以分类
    override fun resetDeviceMemory(dev: DeviceBean) {
        isShowDialog.value = false
    }

    private fun updateWarningList(deviceBean: DeviceBean, str: String) {
        if (!deviceBean.isZigBeeWifi) {
            //TODO Room保存，待写
            val list = warningList.value.toMutableList()
            list.add(str)
            warningList.value = list
        }
    }

    suspend fun removeDeviceSet() {
        mutex.withLock {
            val set = removeSet.value.toMutableSet()
            removeDeviceBeanListBySet(set)
            set.forEach {
                try {
//                    resetDevice(it)
                    removeDevice(it)
                } catch (e: Exception) {
                    println(e)
                }
            }
        }
        removeSet.value = mutableSetOf()
    }

    suspend fun removeDeviceBeanListBySet(set: Set<String>) {
        val list = deviceList.value.toMutableList()
        deviceList.value = list.filter {
            Log.e(
                TAG,
                "removeDeviceBeanBySet: ${!set.contains(it.devId)}, ${it.devId}, ${set.toString()}"
            )
            !set.contains(it.devId)
        }
        Log.e(TAG, "removeDeviceBeanBySet: ${deviceList.value.toMutableList().size}")
    }

    //没权限
    suspend fun resetDevice(deviceId: String): Boolean {
        return suspendCoroutine { continuation ->
            val mDevice = TuyaHomeSdk.newDeviceInstance(deviceId)
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

    var childBuilder: TuyaGwSubDevActivatorBuilder? = null
    var mTuyaGWSubActivator: ITuyaActivator? = null
    private val step = MutableStateFlow("")
    suspend fun startSearch(devId: String): DeviceBean {
        return suspendCoroutine { continuation ->
            Log.e(
                TAG,
                "startSearch: $devId， deviceList.value.toMutableList() ：${deviceList.value.toMutableList().size}"
            )
            childBuilder = TuyaGwSubDevActivatorBuilder()
                .setDevId(devId)
                .setTimeOut(60)
                .setListener(object : ITuyaSmartActivatorListener {
                    override fun onError(errorCode: String?, errorMsg: String?) {
                        Toast.makeText(MyApplication.context, "搜索失败，请重试", Toast.LENGTH_SHORT).show()
                        Log.e(TAG, "onError: 获取子设备失败,$errorCode, $errorMsg")
                        continuation.resumeWithException(Exception("$errorCode,$errorMsg"))
                    }

                    override fun onActiveSuccess(devResp: DeviceBean) {
                        Log.e(
                            TAG,
                            "FakePostsRepository onActiveSuccess: 获取子设备成功，名字：${devResp.name}，功能点：${
                                getDsp(
                                    devResp
                                )
                            }"
                        )

                        continuation.resume(devResp)
                    }

                    override fun onStep(step: String?, data: Any?) {
                        Log.e(TAG, "onStep: 正在继续，please wait ：$step，$data")
                    }

                })
            mTuyaGWSubActivator =
                TuyaHomeSdk.getActivatorInstance().newGwSubDevActivator(childBuilder)
//开始配网
            mTuyaGWSubActivator?.start()
        }
    }

    suspend fun addDeviceList(device: DeviceBean) {
        mutex.withLock {
            val list = deviceList.value.toMutableList()
            list.add(device)
            deviceList.value = list
        }
    }

    suspend fun rename(deviceId: String?, newName: String): Boolean {
        return suspendCoroutine { continuation ->
            if (deviceId == null) {
                continuation.resume(false)
            }
            deviceId?.let {
                val mDevice = TuyaHomeSdk.newDeviceInstance(deviceId)
                mDevice.renameDevice(newName, object : IResultCallback {
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

    fun getDevBeanById(deviceId: String): DeviceBean? {
        val list = deviceList.value
        list.forEach {
            if (it.devId == deviceId) {
                return it
            }
        }
        return null
    }

    fun getZigbeeGateList(): List<DeviceBean> {
        return deviceList.value.filter { it.isZigBeeWifi }
    }
}