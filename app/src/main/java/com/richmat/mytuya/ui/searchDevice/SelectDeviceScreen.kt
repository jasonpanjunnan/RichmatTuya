package com.richmat.mytuya.ui.searchDevice

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.*
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Alignment.Companion.CenterHorizontally
import androidx.compose.ui.Alignment.Companion.CenterVertically
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import com.example.compose.jetsurvey.theme.JetsurveyTheme
import com.example.compose.jetsurvey.theme.Red300
import com.example.compose.jetsurvey.theme.Red800
import com.example.compose.jetsurvey.theme.Write300
import com.richmat.mytuya.R
import com.richmat.mytuya.ui.components.SignInSignUpTopAppBar
import com.richmat.mytuya.ui.newHome.Page
import com.richmat.mytuya.util.data.DevResultMassage
import com.tuya.smart.sdk.bean.DeviceBean

@ExperimentalFoundationApi
@Composable
fun SelectDeviceScreen(
    navController: NavHostController,
    viewModel: SelectDeviceViewModel,
) {
    val uiState by viewModel.uiState.collectAsState()
    val position = uiState.position

    var showDialog by remember {
        mutableStateOf(false)
    }
    val navigateSearch = {
        navController.navigate("${Page.SearchDevice.route}/${uiState.homeId}")
    }

    Scaffold(
        topBar = {
            SignInSignUpTopAppBar(topAppBarText = stringResource(id = R.string.manually_add),
                onBackPressed = { navController.navigateUp() })
        }
    ) {
        Row(modifier = Modifier.fillMaxSize()) {
            LazyColumn(
                modifier = Modifier
                    .weight(3f)
                    .background(Write300)
                    .fillMaxHeight()
            ) {
                itemsIndexed(uiState.getDeviceList()) { index, item ->
                    var selectColor by mutableStateOf(Color.Black)
                    if (position == index) {
                        selectColor = Red300
                    }
                    TextButton(
                        onClick = { viewModel.changeSelectItem(index) },
                        modifier = Modifier
                            .fillMaxWidth()
                            .align(CenterVertically)
                            .heightIn(min = 80.dp)
                    ) {
                        Text(text = stringResource(id = item), color = selectColor)
                    }
                }
            }

            LazyVerticalGrid(
                cells = GridCells.Fixed(3),
                modifier = Modifier
                    .weight(9f)
                    .fillMaxHeight()
//                    .background(Red800)
                    .align(alignment = CenterVertically)
            ) {
                items(uiState.getSelectDeviceList(position = position)) { item ->
                    DeviceShow(
                        modifier = Modifier
                            .align(CenterVertically)
                            .clickable {
                                if (item.needGate) {
                                    showDialog = true
                                } else {
//                                    val navigateSearch = navController.navigate("${Page.SearchDevice.route}/${uiState.homeId}")
                                    navigateSearch()
                                }
                            }
                            .padding(14.dp), device = item
                    )
                }
            }
        }
    }

    if (showDialog) {
        SelectGateDialog(
            dismiss = { showDialog = false },
            zigbeeGateList = uiState.zigbeeGateList,
            navigate = {
                viewModel.getRoute(it)?.let { route ->
                    navController.navigate("$route/${"yingyingying"}") {
                        popUpTo(navController.graph.findStartDestination().id) {
                            //此项可控制是否退出首项 StartDestination
//                    inclusive = true
//                        saveState = true
                        }
                        launchSingleTop = true
                        restoreState = true
                    }
                }
            },
            navigateSearch = navigateSearch
        )
    }
}

@Composable
fun SelectGateDialog(
    modifier: Modifier = Modifier,
    dismiss: () -> Unit = {},
    zigbeeGateList: List<DeviceBean>,
    navigate: (String) -> Unit,
    navigateSearch: () -> Unit,
) {
    AlertDialog(
        modifier = modifier.wrapContentWidth(),
        onDismissRequest = {},
        confirmButton = {},
        dismissButton = {
//            Divider()
            TextButton(onClick = dismiss,
                modifier = Modifier
                    .fillMaxWidth()
                    .wrapContentWidth(CenterHorizontally)) {
                Text(text = stringResource(id = R.string.cancel), color = Red300, fontSize = 18.sp)
            }
        },
        title = {
            Text(
                text = stringResource(id = R.string.select_gate),
                modifier = Modifier
                    .fillMaxWidth()
                    .wrapContentWidth(CenterHorizontally),
                fontSize = 21.sp
            )
        },
        text = {
            Column(horizontalAlignment = CenterHorizontally) {
                if (zigbeeGateList.isNotEmpty()) {
                    Text(text = stringResource(id = R.string.please_select_gate))
                    Spacer(modifier = Modifier.height(8.dp))
                    LazyColumn {
                        items(zigbeeGateList) { item ->
                            TextButton(onClick = { navigate(item.devId) }) {
                                Text(text = item.name, color = Red800, fontSize = 18.sp)
                            }
                            Divider()
                        }
                    }
                } else {
                    Text(text = stringResource(id = R.string.please_configure_gate))
                    Spacer(modifier = Modifier.height(8.dp))
                    TextButton(
                        onClick = navigateSearch,
                        modifier = Modifier
                            .fillMaxWidth()
                            .wrapContentWidth(CenterHorizontally),
                    ) {
                        Text(text = stringResource(id = R.string.configure_wireless_gate),
                            color = Red800,
                            fontSize = 18.sp)
                    }
                    Divider()
                }
            }
        })
}

@Composable
fun DeviceShow(modifier: Modifier = Modifier, device: DeviceItem) {
    Column(modifier = modifier, horizontalAlignment = Alignment.CenterHorizontally) {
        Image(imageVector = device.painter, contentDescription = "")
        Spacer(modifier = Modifier.height(10.dp))
        Text(text = stringResource(id = device.name), fontSize = 13.sp)
        Text(text = "(${stringResource(id = device.type)})", fontSize = 12.sp)
    }
}

@ExperimentalFoundationApi
@Preview
@Composable
fun ShowScreen() {
    JetsurveyTheme {
        //能提还是尽量往外提
        SelectGateDialog(zigbeeGateList = emptyList(), navigate = {}, navigateSearch = {})
    }
}