package com.richmat.mytuya.ui.device.gateway

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.layout.*
import androidx.compose.material.Scaffold
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import com.example.compose.jetsurvey.theme.Red800
import com.richmat.mytuya.R
import com.richmat.mytuya.ui.components.FullScreenLoading
import com.richmat.mytuya.ui.components.SignInSignUpTopAppBar

@ExperimentalAnimationApi
@Composable
fun SearchDeviceChildScreen(
    navController: NavHostController,
    viewModel: ChildSearchViewModel,
    modifier: Modifier = Modifier
) {
    val uiState by viewModel.uiState.collectAsState()
//    val setupTwo = uiState.
    Scaffold(
        topBar = {
            SignInSignUpTopAppBar(
                topAppBarText = "搜索设备",
                onBackPressed = { navController.navigateUp() })
        }
    ) {
        Box {
            Column(
                modifier = modifier
                    .align(Alignment.TopCenter)
                    .fillMaxWidth()
                    .padding(top = 20.dp), horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(text = "正在发现附近的设备")
                Spacer(modifier = Modifier.height(5.dp))
                Text(text = "确保设备处于陪网状态")
                if (uiState.success) {
                    Text(
                        text = stringResource(id = R.string.connect_success),
                        modifier = Modifier
                            .fillMaxSize()
                            .wrapContentSize(Alignment.Center),
                        fontSize = 40.sp,
                        color = Red800
                    )
                } else {
                    FullScreenLoading()
                }
            }

            Row(
                modifier = modifier
                    .align(Alignment.BottomCenter)
                    .padding(start = 20.dp, bottom = 100.dp, end = 20.dp)
            ) {
                AnimatedVisibility(
                    visible = true,
                    enter = fadeIn(),
                    exit = fadeOut(),
                    modifier = Modifier.weight(1f)
                ) {
                    Text(text = "已搜索到设备")
                }
//                Spacer(modifier = Modifier.width(20.dp))
                AnimatedVisibility(
                    visible = true,
                    enter = fadeIn(),
                    exit = fadeOut(),
                    modifier = Modifier.weight(1f)
                ) {
                    Text(text = "连接设备中")
                }
//                Spacer(modifier = Modifier.width(20.dp))
                AnimatedVisibility(
                    visible = true,
                    enter = fadeIn(),
                    exit = fadeOut(),
                    modifier = Modifier.weight(1f)
                ) {
                    Text(text = "初始化设备中")
                }
            }
        }
    }
}