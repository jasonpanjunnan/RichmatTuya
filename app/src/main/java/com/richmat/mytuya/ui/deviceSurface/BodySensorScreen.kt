package com.richmat.mytuya.ui.deviceSurface

import android.util.Log
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Alignment.Companion.Center
import androidx.compose.ui.Alignment.Companion.CenterVertically
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import com.richmat.mytuya.ui.theme.JetsurveyTheme
import com.example.compose.jetsurvey.theme.Red800
import com.example.compose.jetsurvey.theme.Write300
import com.richmat.mytuya.R
import com.richmat.mytuya.util.data.TAG
import com.richmat.mytuya.ui.components.SignInSignUpTopAppBar
import com.richmat.mytuya.ui.deviceSurface.viewModel.BodySensorViewModel

@ExperimentalAnimationApi
@Composable
fun BodySensorScreen(
    navController: NavHostController,
    viewModel: BodySensorViewModel = androidx.lifecycle.viewmodel.compose.viewModel()
) {
    val uiState by viewModel.uiState.collectAsState()
    val warningList = uiState.warningsList
    val hasPeople = uiState.isShowWarning
    Scaffold(
        topBar = {
            SignInSignUpTopAppBar(
                topAppBarText = stringResource(id = R.string.sign_up),
                onBackPressed = { navController.navigateUp() }
            )
        }
    ) {
        Log.e(TAG, "BodySensorScreen:hasPeople $hasPeople")
        FunctionScreen(
            warningList = warningList,
            hasPeople = hasPeople,
            removeDevice = { viewModel.removeDevice() },
            removeListener = { viewModel.removeListener() },
            reset = { viewModel.resetWarning() },
        )
    }
}

@ExperimentalAnimationApi
@Composable
fun FunctionScreen(
    modifier: Modifier = Modifier,
    warningList: List<String>,
    hasPeople: Boolean,
    removeDevice: () -> Unit,
    removeListener: () -> Unit,
    reset: () -> Unit
) {
    Column(
        modifier = modifier
            .fillMaxSize()
    ) {
        Card(
            modifier = Modifier
                .fillMaxSize()
                .padding(7.dp)
                .weight(1f),
            backgroundColor = Color.Yellow
        ) {
            Row(modifier = Modifier) {
                Button(onClick = removeDevice, modifier = Modifier.align(CenterVertically)) {
                    Text(modifier = Modifier.align(Alignment.CenterVertically), text = "移除设备")
                }

                Button(onClick = removeListener, modifier = Modifier.align(CenterVertically)) {
                    Text(modifier = Modifier.align(Alignment.CenterVertically), text = "取消监听")
                }

                Button(onClick = removeListener, modifier = Modifier.align(CenterVertically)) {
                    Text(modifier = Modifier.align(Alignment.CenterVertically), text = "设备重命名")
                }

                Button(onClick = removeListener, modifier = Modifier.align(CenterVertically)) {
                    Text(modifier = Modifier.align(Alignment.CenterVertically), text = "设备重命名")
                }

                Button(onClick = removeListener, modifier = Modifier.align(CenterVertically)) {
                    Text(modifier = Modifier.align(Alignment.CenterVertically), text = "设备重命名")
                }
            }
        }
        Card(
            modifier = Modifier
                .fillMaxSize()
                .padding(7.dp)
                .weight(1f),
            backgroundColor = Color.Blue
        ) {
            Log.e(TAG, "BodySensorScreen:hasPeople $hasPeople")
            LazyColumn() {
                items(warningList) { str ->
                    Text(text = str, color = Write300)
                    Spacer(modifier = Modifier.height(5.dp))
                }
            }
        }
        Card(
            modifier = Modifier
                .fillMaxSize()
                .padding(7.dp)
                .weight(1f),
            backgroundColor = Red800
        ) {
            AnimatedVisibility(visible = hasPeople) {
                Box(contentAlignment = Center) {
                    Text(
                        text = "有人经过",
                        color = Color.Black,
                        style = MaterialTheme.typography.subtitle2,
                        fontSize = 40.sp
                    )
                }
                reset()
            }
        }
    }
}

@ExperimentalAnimationApi
@Preview
@Composable
fun ShowBody() {
    JetsurveyTheme {
        BodySensorScreen(rememberNavController())
    }
}
