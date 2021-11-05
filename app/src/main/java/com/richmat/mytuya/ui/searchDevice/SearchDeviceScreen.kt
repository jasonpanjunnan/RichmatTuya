package com.richmat.mytuya.ui.searchDevice

import android.os.Build
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.sharp.TaskAlt
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Alignment.Companion.CenterHorizontally
import androidx.compose.ui.Alignment.Companion.End
import androidx.compose.ui.Alignment.Companion.Start
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import com.alibaba.fastjson.JSON
import com.example.compose.jetsurvey.theme.JetsurveyTheme
import com.example.compose.jetsurvey.theme.Red300
import com.example.compose.jetsurvey.theme.Red800
import com.google.accompanist.navigation.animation.rememberAnimatedNavController
import com.richmat.mytuya.MyApplication
import com.richmat.mytuya.R
import com.richmat.mytuya.ui.components.FullScreenLoading
import com.richmat.mytuya.ui.newHome.Page
import kotlinx.coroutines.InternalCoroutinesApi

@ExperimentalMaterialApi
@InternalCoroutinesApi
@RequiresApi(Build.VERSION_CODES.O)
@ExperimentalAnimationApi
@Composable
fun SearchDeviceScreen(
    navController: NavHostController,
    searchDeviceViewModel: SearchDeviceViewModel,
) {
//    var select by remember { mutableStateOf(0) }
//    TabRow(selectedTabIndex = select) {
//
//    }
//    
//    Box(
//        modifier = Modifier
//            .fillMaxHeight()
//            .wrapContentWidth()
//            .background(Color.Yellow),
//        contentAlignment = Alignment.Center,
//    ) {
//        Text(text = "搜索界面", style = MaterialTheme.typography.subtitle1)
//    }
    val searchUiState by searchDeviceViewModel.uiState.collectAsState()
    val wifiState by searchDeviceViewModel.wifiState.collectAsState()
    val result by searchDeviceViewModel.searchResult.collectAsState()
    val searchStep by searchDeviceViewModel.searchStep.collectAsState()

//    TODO 根据结果打开Result界面，周一写，周一没时间写，周二写
    when (searchUiState.searchFinish) {
        SearchResult.Fail -> TODO()
        SearchResult.NoSearch -> TODO()
        SearchResult.Success -> {
            val devJson = JSON.toJSONString(searchUiState.devResultMassage)
//            val devJson = JSON.toJSONString(DevResultMassage(name = searchUiState.searchDevName, route = searchUiState.route!!))
            navController.navigate("${Page.SearchUiState.route}/${devJson}")
            searchDeviceViewModel.changeResult(null)
        }
        null -> {
        }
    }
    //TODO 等等hilt
//    Log.e(TAG, "SearchDeviceScreen: isShowWarning: ${searchUiState.isShowWarning}")
//    ShowWarningDialog(isShowWarning = searchUiState.isShowWarning) {}
    val sssss = searchUiState.isShowWarning

//    val searchState = searchUiState.searchState[searchUiState.currentQuestionIndex]
    val searchState = remember(searchUiState.currentQuestionIndex) {
        searchUiState.searchState[searchUiState.currentQuestionIndex]
    }


    Column(modifier = Modifier.padding(9.dp)) {

        Text(
            text = stringResource(id = R.string.cancel),
            modifier = Modifier
                .padding(start = 5.dp)
                .clickable {
                    navController.navigateUp()
                    searchDeviceViewModel.stopSearch()
                }
        )
        Spacer(modifier = Modifier.height(10.dp))
        Card(modifier = Modifier.fillMaxSize()) {
            Spacer(modifier = Modifier.height(10.dp))
            when (searchState.searchDetail.search) {
                is Search.Wifi -> {
                    SelectWifi(
                        rememberWifiName = wifiState.first,
                        rememberWifiPassword = wifiState.second,
                        onChangeName = { name ->
                            searchDeviceViewModel.onChangeName(name)
                        },
                        onChangePassword = { password ->
                            searchDeviceViewModel.onChangePassword(password)
                        }
                    ) { wifiName, wifiPassword ->
//                        searchUiState.currentQuestionIndex++
                        searchDeviceViewModel.rememberAccount(
                            wifiName = wifiName,
                            wifiPassword = wifiPassword
                        )
                        searchDeviceViewModel.getToken()
                    }
                }
                is Search.GetToken -> {
                    ResetDeviceScreen(
                        click = {
                            searchUiState.currentQuestionIndex++
                            searchDeviceViewModel.startSearch()
                        },
                        isGetSuccess = searchUiState.isSuccessGetToken
                    )
                }
                is Search.ConnectSurface -> {
                    AddDevice(
                        searchStep = searchStep,
                        searchResult = result,
                    )
                }
            }
        }
    }
}

@Composable
private fun SelectWifi(
    rememberWifiName: String = "",
    rememberWifiPassword: String = "",
    onChangeName: (String) -> Unit,
    onChangePassword: (String) -> Unit,
    click: (String, String) -> Unit,
) {
//    var wifiName by remember { mutableStateOf(rememberWifiName) }
//    var wifiPassword by remember { mutableStateOf(rememberWifiPassword) }
//    val isClick: Boolean = wifiName.isNotEmpty() && wifiPassword.isNotEmpty()
    val isClick: Boolean = rememberWifiName.isNotEmpty() && rememberWifiPassword.isNotEmpty()
    Column(
        modifier = Modifier.padding(10.dp),
        horizontalAlignment = CenterHorizontally
    ) {
        Text(
            text = stringResource(id = R.string.select_2_4g),
            fontSize = 25.sp,
            style = MaterialTheme.typography.subtitle1,
            modifier = Modifier
                .align(CenterHorizontally)
                .padding(20.dp)
        )
        Spacer(modifier = Modifier.height(10.dp))
        Text(
            text = stringResource(id = R.string.hint_2_4g),
            style = MaterialTheme.typography.body1,
            modifier = Modifier.align(CenterHorizontally)
        )
        Spacer(modifier = Modifier.height(100.dp))
//        OutlinedTextField(value = wifiName,
//            onValueChange = {
//                wifiName = it
//            }, label = { Text(text = "Wifi 名称") })
        OutlinedTextField(value = rememberWifiName,
            onValueChange = { onChangeName(it) }, label = { Text(text = "Wifi 名称") })
        Spacer(modifier = Modifier.height(6.dp))
        OutlinedTextField(value = rememberWifiPassword,
            onValueChange = {
                onChangePassword(it)
            }, label = { Text(text = "Wifi 密码") })
        Spacer(modifier = Modifier.height(16.dp))
        Button(
            onClick = {
                if (isClick) {
                    click(rememberWifiName, rememberWifiPassword)
                } else {
                    Toast.makeText(MyApplication.context, "wifi名称和密码不能为空", Toast.LENGTH_SHORT)
                        .show()
                }
            },
            modifier = Modifier.fillMaxWidth(0.8f)
        ) {
            Text(text = stringResource(id = R.string.next))
        }
    }
}

@Composable
fun ResetDeviceScreen(modifier: Modifier = Modifier, click: () -> Unit, isGetSuccess: Boolean) {
    Column(
        modifier = modifier.padding(10.dp),
        horizontalAlignment = CenterHorizontally
    ) {
        Text(
            text = stringResource(id = R.string.reset),
            fontSize = 25.sp,
            style = MaterialTheme.typography.subtitle1,
            modifier = Modifier
                .align(Alignment.Start)
                .padding(20.dp)
        )
        Spacer(modifier = Modifier.height(10.dp))
        Text(
            text = stringResource(id = R.string.reset_hint),
            style = MaterialTheme.typography.body1,
            modifier = Modifier
                .align(Alignment.Start)
                .padding(start = 15.dp)
        )

        Spacer(modifier = Modifier.height(30.dp))
        Image(painter = painterResource(id = R.drawable.gate_image), contentDescription = "gate")
        Spacer(modifier = Modifier.height(20.dp))

        Row() {
            //token不为空时，才显示
            Button(
                enabled = isGetSuccess,
                onClick = click,
                modifier = Modifier
                    .padding(20.dp)
                    .weight(1f)
//                .align(Alignment.End)
                    .fillMaxWidth(0.8f)
            ) {
                Text(text = stringResource(id = R.string.next))
            }
        }
    }
}

@ExperimentalMaterialApi
@ExperimentalAnimationApi
@Composable
private fun AddDevice(
    modifier: Modifier = Modifier,
    searchStep: ConnectStep,
    searchResult: Boolean,
) {

    val value by animateFloatAsState(
        when (searchStep) {
            is ConnectStep.ConnectFirst -> 0f
            is ConnectStep.ConnectSecond -> 0.5f
            is ConnectStep.ConnectSuccess -> 1f
        }
    )
    val finish = value == 1f
    Column(
        modifier = Modifier.padding(10.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {

        Text(
            text = stringResource(id = R.string.connect_device),
            fontSize = 25.sp,
            style = MaterialTheme.typography.subtitle1,
            modifier = Modifier
                .align(Alignment.Start)
                .padding(20.dp)
        )
        Spacer(modifier = Modifier.height(10.dp))
        Text(
            text = stringResource(id = R.string.connect_device_hint),
            style = MaterialTheme.typography.body1,
            modifier = Modifier.align(Alignment.CenterHorizontally)
        )

        if (!searchResult) {
            FullScreenLoading(
                modifier = Modifier
                    .wrapContentSize()
                    .weight(1f)
            )
            Text(
                text = stringResource(id = R.string.connecting),
                modifier = Modifier.wrapContentSize(),
                color = Color.Black.copy(alpha = 0.5f)
            )
        } else if (!finish) {
            Text(
                text = stringResource(id = R.string.connect_success_init),
                modifier = Modifier
                    .fillMaxSize()
                    .weight(1f)
                    .wrapContentSize(Alignment.Center),
                fontSize = 30.sp,
                color = Red800
            )
        } else {
            Icon(
                modifier = Modifier
                    .weight(1f)
                    .size(65.dp),
                imageVector = Icons.Sharp.TaskAlt,
                contentDescription = "", tint = Red300
            )
        }

        AnimatedVisibility(
            visible = searchResult,
            modifier = Modifier
                .align(Alignment.End)
                .padding(bottom = 50.dp)
                .padding(horizontal = 10.dp)
        ) {
            //TODO 进度条
            Column() {
                Slider(
                    modifier = Modifier.padding(horizontal = 30.dp),
                    value = value, onValueChange = {}, colors = SliderDefaults.colors(
                        thumbColor = Color(0xFF009688),
                        activeTickColor = Color(0xFFFF5722),
                        activeTrackColor = Color(0xFF03A9F4)
                    )
                )
                Row(
                    modifier = modifier
                        .fillMaxWidth()
                        .wrapContentHeight()
                ) {
                    AnimatedVisibility(
                        visible = value == 0f,
                        enter = fadeIn(),
                        exit = fadeOut(),
                        modifier = Modifier
                            .fillMaxWidth()
                            .wrapContentWidth(Start)
                    ) {
                        Text(text = stringResource(id = R.string.searched), fontSize = 11.sp)
                    }
//                Spacer(modifier = Modifier.width(20.dp))
                    AnimatedVisibility(
                        visible = value == 0.5f,
                        enter = fadeIn(),
                        exit = fadeOut(),
                        modifier = Modifier
                            .fillMaxWidth()
                            .wrapContentWidth(CenterHorizontally)
                    ) {
                        Text(
                            text = stringResource(id = R.string.connecting_device),
                            fontSize = 11.sp
                        )
                    }
//                Spacer(modifier = Modifier.width(20.dp))
                    AnimatedVisibility(
                        visible = finish,
                        enter = fadeIn(),
                        exit = fadeOut(),
                        modifier = Modifier
                            .fillMaxWidth()
                            .wrapContentWidth(End)
                    ) {
                        Text(text = stringResource(id = R.string.device_init), fontSize = 11.sp)
                    }
                }
//            Spacer(modifier = Modifier.height(10.dp))
//            RangeSlider(
//                values = (1f..4f), onValueChange = {}, colors = SliderDefaults.colors(
//                    thumbColor = Color(0xFFFFEB3B),
//                    activeTickColor = Color(0xFF00BCD4)
//                )
//            )

//            CircularProgressIndicator(progress = 0.5f, color = Red800, strokeWidth = 6.dp)
//            LinearProgressIndicator(
//                progress = 0.7f,
//                modifier = Modifier
//                    .fillMaxWidth()
//                    .padding(bottom = 20.dp),
//                backgroundColor = Red800
//            )
            }
        }
    }
}

@ExperimentalMaterialApi
@InternalCoroutinesApi
@RequiresApi(Build.VERSION_CODES.O)
@ExperimentalAnimationApi
@Preview
@Composable
fun Show() {
    SearchDeviceScreen(rememberAnimatedNavController(), viewModel())
}

@Preview
@Composable
fun ShowToken() {
    ResetDeviceScreen(click = {}, isGetSuccess = false)
}

@ExperimentalMaterialApi
@ExperimentalAnimationApi
@Preview
@Composable
fun ShowAddDevice() {
    JetsurveyTheme {
        AddDevice(
            searchStep = ConnectStep.ConnectFirst(step = ""),
            searchResult = true
        )
    }
}