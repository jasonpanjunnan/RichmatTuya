//package com.richmat.mytuya
//
//import android.content.Context
//import android.os.Bundle
//import android.util.Log
//import android.widget.Toast
//import androidx.activity.ComponentActivity
//import androidx.activity.compose.setContent
//import androidx.compose.animation.AnimatedVisibility
//import androidx.compose.animation.ExperimentalAnimationApi
//import androidx.compose.foundation.clickable
//import androidx.compose.foundation.gestures.*
//import androidx.compose.foundation.layout.*
//import androidx.compose.foundation.lazy.LazyColumn
//import androidx.compose.foundation.lazy.items
//import androidx.compose.foundation.lazy.rememberLazyListState
//import androidx.compose.material.*
//import androidx.compose.material.icons.Icons
//import androidx.compose.material.icons.rounded.Add
//import androidx.compose.runtime.*
//import androidx.compose.ui.Modifier
//import androidx.compose.ui.tooling.preview.Preview
//import androidx.compose.ui.unit.dp
//import androidx.lifecycle.viewmodel.compose.viewModel
//import com.richmat.mytuya.ui.theme.JetsurveyTheme
//import com.richmat.mytuya.ui.newHome.homeViewmoel.HomeViewModel
//import com.richmat.mytuya.util.data.Home
//import com.richmat.mytuya.util.data.Home.currentHomeBean
//import com.tuya.smart.home.sdk.TuyaHomeSdk
//import com.tuya.smart.home.sdk.bean.HomeBean
//import com.tuya.smart.home.sdk.builder.ActivatorBuilder
//import com.tuya.smart.home.sdk.builder.TuyaGwSubDevActivatorBuilder
//import com.tuya.smart.home.sdk.callback.ITuyaGetHomeListCallback
//import com.tuya.smart.home.sdk.callback.ITuyaHomeResultCallback
//import com.tuya.smart.sdk.api.IDevListener
//import com.tuya.smart.sdk.api.ITuyaActivator
//import com.tuya.smart.sdk.api.ITuyaActivatorGetToken
//import com.tuya.smart.sdk.api.ITuyaSmartActivatorListener
//import com.tuya.smart.sdk.bean.DeviceBean
//import com.tuya.smart.sdk.enums.ActivatorModelEnum
//
//val TAG = "colin"
//var homeList = ArrayList<HomeBean>()
//
//class HomeActivity : ComponentActivity() {
//    @ExperimentalAnimationApi
//    override fun onCreate(savedInstanceState: Bundle?) {
//        super.onCreate(savedInstanceState)
//        setContent {
//            JetsurveyTheme {
//                NewHomeScreen(activity = this)
////                HomeScreen("") {
////                    TuyaHomeSdk.getHomeManagerInstance().createHome(
////                        "rich",
////                        3.3,
////                        3.3,
////                        "nihao",
////                        listOf(),
////                        object : ITuyaHomeResultCallback {
////                            override fun onSuccess(bean: HomeBean?) {
////                                currentHomeBean = bean
////                                Log.e(TAG, "onSuccess: ")
////                            }
////
////                            override fun onError(errorCode: String?, errorMsg: String?) {
////                                Toast.makeText(
////                                    this@HomeActivity,
////                                    "创建失败，请重试！",
////                                    Toast.LENGTH_SHORT
////                                )
////                                    .show()
////                                Log.e(TAG, "onError: ")
////                            }
////                        }
////                    )
////                }
//            }
//        }
//    }
//
//    override fun onResume() {
//        super.onResume()
//
////        try {
//        TuyaHomeSdk.getHomeManagerInstance().queryHomeList(object : ITuyaGetHomeListCallback {
//            override fun onSuccess(homeBeans: MutableList<HomeBean>?) {
//                homeList = homeBeans as ArrayList<HomeBean>
//                if (homeList.size > 0) {
//
//                    currentHomeBean = homeList[0]
//                }
//                Log.e(TAG, "onSuccess: 家庭数量为 ${homeList.size}")
//            }
//
//            override fun onError(errorCode: String?, error: String?) {
//                Toast.makeText(this@HomeActivity, "没有发现家庭，请创建！", Toast.LENGTH_SHORT).show()
//                Log.e(TAG, "onError: 列表为空")
//            }
//        })
////
////        } catch (e: Exception) {
////            println(e)
////        }
//
//    }
//}
//
//@ExperimentalAnimationApi
//@Composable
//fun NewHomeScreen(viewModel: HomeViewModel = viewModel(), activity: Context) {
//    val token: String by viewModel.mToken.collectAsState()
//    HomeScreen(
//        mToken = token, viewModel = viewModel, activity,
//        click = {
//            TuyaHomeSdk.getHomeManagerInstance().createHome(
//                "rich",
//                3.3,
//                3.3,
//                "nihao",
//                listOf(),
//                object : ITuyaHomeResultCallback {
//                    override fun onSuccess(bean: HomeBean?) {
//                        currentHomeBean = bean
//                        Log.e(TAG, "onSuccess: ")
//                    }
//
//                    override fun onError(errorCode: String?, errorMsg: String?) {
//                        Toast.makeText(
//                            MyApplication.context,
//                            "创建失败，请重试！",
//                            Toast.LENGTH_SHORT
//                        )
//                            .show()
//                        Log.e(TAG, "onError: ")
//                    }
//                }
//            )
//        },
//    )
//}
//
//private lateinit var mTuyaActivator: ITuyaActivator
//
//@Composable
//fun AddHome(click: () -> Unit) {
//    IconButton(onClick = click) {
//        Icon(Icons.Rounded.Add, contentDescription = "Add")
//    }
//}
//
//@ExperimentalAnimationApi
//@Composable
//fun HomeScreen(
//    mToken: String,
//    viewModel: HomeViewModel,
//    activity: Context,
//    click: () -> Unit,
//) {
//    var wifiName by remember {
//        mutableStateOf("")
//    }
//    var wifiPassword by remember {
//        mutableStateOf("")
//    }
//    var isGatewayFind by remember {
//        mutableStateOf(false)
//    }
//    var builder: ActivatorBuilder? = null
//
//    Column(modifier = Modifier.padding(16.dp)) {
//        AddHome(click)
//        Spacer(modifier = Modifier.height(8.dp))
//        Button(onClick = {
//            if (currentHomeBean?.homeId != null) {
//                TuyaHomeSdk.getActivatorInstance()
//                    .getActivatorToken(currentHomeBean?.homeId!!, object : ITuyaActivatorGetToken {
//                        override fun onSuccess(token: String) {
//                            viewModel.onNewChange(token)
//                            Home.token = token
//                            Log.e(TAG, "onSuccess: $token")
//                        }
//
//                        override fun onFailure(errorCode: String?, errorMsg: String?) {
//                            Log.e(TAG, "onFailure: $errorCode, $errorMsg")
//                        }
//                    })
//            } else {
//                Log.e(TAG, "HomeScreen: homeId == null")
//            }
//        }) {
//            Text(text = "获取Token")
//        }
//        Spacer(modifier = Modifier.height(8.dp))
//        AnimatedVisibility(visible = mToken.isNotEmpty()) {
//            Column() {
//                Text(
//                    text = mToken,
//                    modifier = Modifier.padding(5.dp),
//                    style = MaterialTheme.typography.h5
//                )
//                Spacer(modifier = Modifier.height(6.dp))
//                Text(
//                    text = "连接热点前，请先连接热点：SmartLife- 或者 SL-",
//                    modifier = Modifier.padding(5.dp),
//                    style = MaterialTheme.typography.h5
//                )
//            }
//        }
//        AnimatedVisibility(visible = mToken.isNotEmpty()) {
//            Column() {
//
//                OutlinedTextField(value = wifiName,
//                    onValueChange = {
//                        wifiName = it
//                    }, label = { Text(text = "Wifi 名称") })
//                Spacer(modifier = Modifier.height(6.dp))
//                OutlinedTextField(value = wifiPassword,
//                    onValueChange = {
//                        wifiPassword = it
//                    }, label = { Text(text = "Wifi 密码") })
//                Spacer(modifier = Modifier.height(6.dp))
//                Button(onClick = {
//                    if (wifiName.isNotEmpty() && wifiPassword.isNotEmpty()) {
//                        builder = ActivatorBuilder()
//                            .setContext(activity)
//                            .setSsid(wifiName)
//                            .setPassword(wifiPassword)
//                            .setActivatorModel(ActivatorModelEnum.TY_AP)
//                            .setTimeOut(100)
//                            .setToken(mToken)
//                            .setListener(object : ITuyaSmartActivatorListener {
//                                override fun onError(errorCode: String?, errorMsg: String?) {
//                                    Log.e(TAG, "onError: $errorCode, $errorMsg")
//                                }
//
//                                override fun onActiveSuccess(devResp: DeviceBean) {
//                                    isGatewayFind = true
//                                    Home.deviceBean = devResp
//                                    val dps =
//                                        TuyaHomeSdk.getDataInstance().getDps(devResp.getDevId())
//                                    var details: String = ""
//                                    dps?.forEach {
//                                        details += "${it.key} + \"\"+ ${it.value}"
//                                    }
//                                    Log.e(
//                                        TAG,
//                                        "onActiveSuccess: ${devResp.name}: $details, ${devResp.devId}"
//                                    )
//                                }
//
//                                override fun onStep(step: String?, data: Any?) {
//                                    Log.e(TAG, "onStep: $step,")
//                                }
//                            })
//                        mTuyaActivator = TuyaHomeSdk.getActivatorInstance().newActivator(builder)
//                        mTuyaActivator.start()
//                    } else {
//                        Toast.makeText(MyApplication.context, "wifi 名称或密码不能为空", Toast.LENGTH_SHORT)
//                            .show()
//                    }
//
//                }) {
//                    Text(text = "热点模式（AP）连接")
//                }
//                Spacer(modifier = Modifier.height(6.dp))
//            }
//        }
//
//        var getListSuccess by remember {
//            mutableStateOf(false)
//        }
//        var zigBeeGatewayList: List<DeviceBean> by remember {
//            mutableStateOf(ArrayList())
//        }
//        //查询前，需要初始化
//        AnimatedVisibility(visible = isGatewayFind) {
//            Column() {
//                Text(text = "获取到网关设备，请继续")
//                Spacer(
//                    modifier = Modifier
//                        .height(6.dp)
//                )
//            }
//        }
//
//        Spacer(modifier = Modifier.height(6.dp))
//        Button(onClick = {
//            currentHomeBean?.homeId?.let {
//                TuyaHomeSdk.newHomeInstance(it)
//                    .getHomeDetail(object : ITuyaHomeResultCallback {
//                        override fun onSuccess(bean: HomeBean) {
//                            zigBeeGatewayList = bean.deviceList
//                            val deviceList: List<DeviceBean> = bean.getDeviceList()
//                            val zigBeeGatewayListNoWifi = deviceList.filter { deviceBean ->
//                                !deviceBean.isZigBeeWifi
//                            }
//                            zigBeeGatewayListNoWifi.forEach {
//                                val device = TuyaHomeSdk.newDeviceInstance(it.devId)
//                                device.registerDevListener(object : IDevListener {
//                                    override fun onDpUpdate(devId: String?, dpStr: String) {
//                                        if (dpStr.contains("pir")) {
//                                            Toast.makeText(
//                                                MyApplication.context,
//                                                "有人经过",
//                                                Toast.LENGTH_SHORT
//                                            ).show()
//                                        }
//                                        Log.e(TAG, "onDpUpdate: devId :$devId, dpstr: $dpStr")
//                                    }
//
//                                    override fun onRemoved(devId: String?) {
//                                        Log.e(TAG, "onRemoved: ")
//                                    }
//
//                                    override fun onStatusChanged(devId: String?, online: Boolean) {
//                                        Log.e(TAG, "onStatusChanged: ")
//                                    }
//
//                                    override fun onNetworkStatusChanged(
//                                        devId: String?,
//                                        status: Boolean
//                                    ) {
//                                        Log.e(TAG, "onNetworkStatusChanged: ")
//                                    }
//
//                                    override fun onDevInfoUpdate(devId: String?) {
//                                        Log.e(TAG, "onDevInfoUpdate: ")
//                                    }
//                                })
//                            }
//
//                            getListSuccess = true
//                            Log.e(
//                                TAG,
//                                "onSuccess: 获取设备成功，${zigBeeGatewayList.size},是否为zigbeeWIFI：${zigBeeGatewayList[0].isZigBeeWifi}, zigBeeGatewayListNoWifi的大小： ${zigBeeGatewayListNoWifi.size}"
//                            )
//                            //下文中的deviceBean就是从这里获取
//                        }
//
//                        override fun onError(errorCode: String, errorMsg: String) {
//                            // do something
//                            Log.e(TAG, "onError: 获取失败")
//                        }
//                    })
//            }
//
//        }) {
//            Text(text = "查询设备列表")
//        }
//
//        AnimatedVisibility(visible = getListSuccess) {
//            val listStates = rememberLazyListState()
//            if (zigBeeGatewayList.isEmpty()) {
//                Text(text = "设备为空")
//            } else {
//                LazyColumn(
//                    state = listStates,
//                    modifier = Modifier.fillMaxWidth(),
//                    contentPadding = PaddingValues(bottom = 80.dp)
//                ) {
//
//                    items(zigBeeGatewayList) { deviceBean ->
//                        ItemHomeBean(deviceBean)
//                    }
//                }
//            }
//        }
//    }
//}
//
//fun getDeviceList() {
//
//}
//
//var childBuilder: TuyaGwSubDevActivatorBuilder? = null
//var mTuyaGWSubActivator: ITuyaActivator? = null
//
//@Composable
//fun ItemHomeBean(bean: DeviceBean) {
//    val dps = getDsp(bean)
//    Text(text = "${bean.name}，$dps",
//        style = MaterialTheme.typography.h4,
//        modifier = Modifier.clickable {
//            childBuilder = TuyaGwSubDevActivatorBuilder()
//                .setDevId(bean.devId)
//                .setTimeOut(60)
//                .setListener(object : ITuyaSmartActivatorListener {
//                    override fun onError(errorCode: String?, errorMsg: String?) {
//                        Toast.makeText(MyApplication.context, "搜索失败，请重试", Toast.LENGTH_SHORT).show()
//                        Log.e(TAG, "onError: 获取子设备失败,$errorCode, $errorMsg")
//                    }
//
//                    override fun onActiveSuccess(devResp: DeviceBean) {
//                        Log.e(
//                            TAG,
//                            "onActiveSuccess: 获取子设备成功，名字：${devResp.name}，功能点：${getDsp(devResp)}"
//                        )
//                    }
//
//                    override fun onStep(step: String?, data: Any?) {
//                        Log.e(TAG, "onStep: 正在继续，please wait ：$step")
//                    }
//
//                })
//            mTuyaGWSubActivator =
//                TuyaHomeSdk.getActivatorInstance().newGwSubDevActivator(childBuilder)
////开始配网
//            mTuyaGWSubActivator?.start()
//        })
//}
//
//fun getDsp(bean: DeviceBean): String {
//    val dps = TuyaHomeSdk.getDataInstance().getDps(bean.getDevId())
//    var details: String = ""
//    dps?.forEach {
//        details += "key: ${it.key} , value :${it.value}"
//    }
//    return details
//}
//
//@ExperimentalAnimationApi
//@Preview
//@Composable
//fun ShowIcon() {
//
//    HomeScreen(
//        "11111", viewModel(),
//        MyApplication.context,
//        {
//            ActivatorModelEnum.TY_EZ
//        },
//    )
//}