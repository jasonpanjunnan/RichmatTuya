package com.richmat.mytuya.ui.device.gateway

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.layout.*
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ModeEdit
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import com.richmat.mytuya.R
import com.richmat.mytuya.ui.components.NewGrid
import com.richmat.mytuya.ui.components.SignInSignUpTopAppBar
import com.richmat.mytuya.ui.deviceSetting.DevSettingInfo
import com.richmat.mytuya.ui.newHome.DevicePage
import com.richmat.mytuya.ui.newHome.Page

@ExperimentalFoundationApi
@Composable
fun GateWayScreen(
    navController: NavHostController,
    gatewayViewModel: GatewayViewModel,
    modifier: Modifier = Modifier,
) {
    val gatewayUiState by gatewayViewModel.uiState.collectAsState()
    val deviceBeans = gatewayUiState.deviceBeans
    val deviceId = gatewayViewModel.deviceId
    Scaffold(
        topBar = {
            SignInSignUpTopAppBar(topAppBarText = stringResource(id = R.string.gateway),
                onBackPressed = { navController.navigateUp() }) {
                IconButton(onClick = {
                    navController.navigate("${Page.DevSetting.route}/$deviceId")
                }) {
                    Icon(
                        imageVector = Icons.Filled.ModeEdit,
                        contentDescription = null
                    )
                }
            }
        }
    ) {
        Box() {
            Column(modifier = Modifier.padding(top = 50.dp)) {
                //TODO 从HomeScreen传入,设备数量与信息. 10.26记 没必要往里传，内部写好即可，一个设备对应一个
                Text(text = "网关", style = MaterialTheme.typography.subtitle2)
                Spacer(modifier = Modifier.height(10.dp))
                Text(text = "在线设备： ${deviceBeans.size} 个", style = MaterialTheme.typography.caption)
                Spacer(modifier = Modifier.height(10.dp))
                Button(onClick = { gatewayViewModel.removeDevice() }) {
                    Text(text = "移除设备")
                }
                NewGrid(list = deviceBeans, navController = navController)
            }

            Box(
                modifier = modifier
                    .fillMaxSize()
                    .align(Alignment.BottomEnd)
            ) {

                Button(
                    enabled = deviceId.isNotEmpty(),
                    onClick = {
                        navController.navigate("${DevicePage.SearchChildDevice.route}/$deviceId")
                    },
                    modifier = modifier
                        .fillMaxWidth()
                        .align(Alignment.BottomEnd)
                ) {
                    Text(text = "搜索设备")
                }
            }
        }
    }
}