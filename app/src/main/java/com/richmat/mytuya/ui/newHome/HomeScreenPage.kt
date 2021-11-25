package com.richmat.mytuya.ui.newHome

import android.content.Context
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.animation.*
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Warning
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Alignment.Companion.CenterHorizontally
import androidx.compose.ui.Alignment.Companion.CenterVertically
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import com.richmat.mytuya.ui.theme.JetsurveyTheme
import com.example.compose.jetsurvey.theme.Red300
import com.example.compose.jetsurvey.theme.Write300
import com.google.accompanist.swiperefresh.SwipeRefresh
import com.google.accompanist.swiperefresh.rememberSwipeRefreshState
import com.richmat.mytuya.R
import com.richmat.mytuya.ui.components.HomeMenu
import com.richmat.mytuya.ui.components.NewGrid
import com.richmat.mytuya.ui.components.NewHomeMenu
import com.richmat.mytuya.ui.newHome.homeViewmoel.Delete
import com.richmat.mytuya.ui.newHome.homeViewmoel.HomeViewModel
import com.tuya.smart.sdk.bean.DeviceBean
import com.tuya.smart.sdk.bean.GroupBean
import kotlinx.coroutines.InternalCoroutinesApi
import java.time.LocalDateTime

//TODO 参考Crane实现折叠状态栏效果
@ExperimentalComposeUiApi
@RequiresApi(Build.VERSION_CODES.O)
@InternalCoroutinesApi
@ExperimentalAnimationApi
@ExperimentalFoundationApi
@Composable
fun HomeScreenPage(
    navController: NavHostController,
    viewModel: HomeViewModel,
) {
    val homeUiState by viewModel.uiState.collectAsState()
    val removeSet = homeUiState.removeSet
    Box(
        modifier = Modifier
            .fillMaxSize(), contentAlignment = Alignment.TopStart
    ) {
        JetsurveyTheme {
            //TODO 不传viewmode，只传状态
            Component(
                navController,
                homeUiState,
                loading = homeUiState.loading,
                onClick = { viewModel.addOrRemove(it) },
                isSelect = { removeSet.contains(it) },
                removeSetState = { removeSet.isEmpty() },
                removeDeviceSet = { viewModel.removeDeviceSet() },
                dismissClick = { viewModel.closeDialog(DeviceBean()) },
                onRefreshDevList = { viewModel.refreshHome() },
                navigate = { deviceId, homeId ->
                    val route = viewModel.getRoute(deviceId)
                    //可选参数
//                    route?.let { navController.navigate("$it?deviceId=$homeId") }
                    route?.let { navController.navigate(it) }
                    //必选参数
//                    route?.let { navController.navigate("$it/$homeId") }
                },
                countries = homeUiState.countries
            )
        }
    }
}

@ExperimentalComposeUiApi
@RequiresApi(Build.VERSION_CODES.O)
@InternalCoroutinesApi
@ExperimentalAnimationApi
@ExperimentalFoundationApi
@Composable
fun Component(
    navController: NavHostController,
    hostState: Delete,
    loading: Boolean,
    onClick: (String) -> Unit,
    isSelect: (String) -> Boolean,
    removeSetState: () -> Boolean,
    removeDeviceSet: () -> Unit,
    dismissClick: () -> Unit,
    onRefreshDevList: () -> Unit,
    navigate: ((String, String) -> Unit)? = null,
    countries: List<String>,
) {
    val homeBean = hostState.bean
    val isShowWarning = hostState.isShowHint
    val eventList = hostState.eventList
    val deviceList = hostState.deviceList

    var removeScreenVisible by rememberSaveable {
        mutableStateOf(false)
    }

    SwipeRefresh(state = rememberSwipeRefreshState(isRefreshing = loading),
        onRefresh = onRefreshDevList) {
        //下拉刷新，需要克滑动的布局.想要整体都能滑动，需要把其他部分，放到网格布局里边（这样也不行，只占了一半），不然滑动冲突
        //解决了，分开装
        Column {
            LazyColumn(state = LazyListState()) {
                item {
                    HomeMenu(navController = navController, homeID = homeBean.homeId)
                    Card(backgroundColor = Color.White, modifier = Modifier.padding(10.dp)) {
                        Box(
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(150.dp)
                                .background(Color.LightGray)
                        ) {
//TODO 天气信息
                        }
                    }
                    TabSelect(
                        Modifier.align(CenterHorizontally),
                        roomList = homeBean.groupList
                    )
                }
            }
            //TODO 点击事件自己写
            if (deviceList.isNotEmpty()) {
                NewGrid(list = deviceList, navController = navController, onLongClick = {
                    removeScreenVisible = true
                }, onClick = navigate)
            }
        }
    }

    AnimatedVisibility(
        visible = removeScreenVisible,
        enter = slideInVertically() + expandVertically(expandFrom = Alignment.Top) + fadeIn(
            initialAlpha = 0.3f
        ),
        exit = slideOutVertically() + shrinkVertically() + fadeOut()
    ) {
        RemoveScreen(
            ondismiss = { removeScreenVisible = false },
            list = deviceList,
            onclick = onClick,
            isSelect = isSelect,
            removeSetState = removeSetState,
            removeDeviceSet = removeDeviceSet
        )
    }
    ShowWarningDialog(isShowWarning, eventList = eventList, dismissClick = dismissClick)
}

@RequiresApi(Build.VERSION_CODES.O)
@InternalCoroutinesApi
@Composable
fun ShowWarningDialog(
    isShowWarning: Boolean,
    eventList: List<String> = listOf("", "", "", LocalDateTime.now().toString()),
    dismissClick: () -> Unit,
) {
    if (isShowWarning) {
        HintDialog(
            dismissClick = dismissClick,
            list = eventList
        )
    }
}

@InternalCoroutinesApi
@Composable
private fun TabSelect(
    modifier: Modifier = Modifier,
    roomList: MutableList<GroupBean>,
) {
    Row(
        modifier = modifier
            .background(MaterialTheme.colors.primary)
//            .align(CenterHorizontally)
    ) {
//        if (roomList.isEmpty()) {
////            return
//        } else {
        ScrollableRomeRow(roomList, modifier = Modifier.weight(9f))
//        }
//        Box(
//            modifier = Modifier
//                .weight(1.5f)
//                .align(Alignment.CenterVertically)
//        ) {
        NewHomeMenu(
            modifier = Modifier
                .weight(1.5f)
                .align(Alignment.CenterVertically)
        )
//        }
    }
}

@Composable
fun AutomaticTab(
    modifier: Modifier = Modifier,
) {
    Row(
        modifier = modifier
            .background(MaterialTheme.colors.primary)
//            .align(CenterHorizontally)
    ) {
        NotScrollableTab(
            automaticList,
            modifier = Modifier
                .weight(9f)
                .align(Alignment.CenterVertically)
        )
        NewHomeMenu(
            modifier = Modifier
                .weight(1.5f)
                .align(Alignment.CenterVertically)
        )
    }
}

@Composable
fun NotScrollableTab(
    list: List<String>,
    modifier: Modifier = Modifier,
) {
    var selected by remember {
        mutableStateOf(0)
    }

    TabRow(
        selectedTabIndex = selected,
        divider = {}, /* Disable the built-in divider */
        modifier = modifier.padding(start = 10.dp)
    ) {
        list.forEachIndexed { index, s ->
            val colorText = if (selected == index) {
                MaterialTheme.colors.onSurface
            } else {
                MaterialTheme.colors.onSurface.copy(alpha = 0.7f)
            }
            Tab(selected = selected == index, onClick = {
                selected = index
//                viewModel.changeLocation(category)
//                    onCategorySelected(category)
            }) {
                Text(
                    text = s,
                    color = colorText,
                    style = MaterialTheme.typography.subtitle2,
                    fontSize = 18.sp
//                    modifier = Modifier.paddingFromBaseline(top = 20.dp)
                )
            }
        }
    }
}

@InternalCoroutinesApi
@Composable
fun ScrollableRomeRow(
    roomList: MutableList<GroupBean>,
    modifier: Modifier = Modifier,
) {
    var selected by remember {
        mutableStateOf(0)
    }
    ScrollableTabRow(
        selectedTabIndex = selected,
        divider = {}, /* Disable the built-in divider */
        edgePadding = 16.dp,
//        indicator = emptyTabIndicator,
        modifier = modifier
    ) {
        if (roomList.isEmpty()) {
//            Text(text = stringResource(id = R.string.all_device))
//            Text(text = stringResource(id = R.string.all_device))
            ChoiceChipContent(text = stringResource(id = R.string.all_device), selected = true)
        } else {
            roomList.forEachIndexed { index, category ->
                Tab(
                    selected = index == selected,
                    onClick = {
                        selected = index
//                    TODO 修改此处实现房间的切换，下面是之前使用假数据的实现
//                    viewModel.changeLocation(category.name)

//                    onCategorySelected(category)
                    }
                ) {
                    ChoiceChipContent(
                        text = category.name,
                        selected = index == selected,
                        modifier = Modifier.padding(horizontal = 4.dp, vertical = 16.dp)
                    )
                }
            }
        }

    }
}

@Composable
fun ChoiceChipContent(text: String, selected: Boolean, modifier: Modifier = Modifier) {
    Surface(
        color = when {
            selected -> MaterialTheme.colors.primary.copy(alpha = 0.08f)
            else -> MaterialTheme.colors.onSurface.copy(alpha = 0.12f)
        },
//        contentColor = when {
//            selected -> MaterialTheme.colors.primary
//            else -> MaterialTheme.colors.onSurface
//        },
        shape = MaterialTheme.shapes.small,
        modifier = modifier
    ) {
        Text(
            text = text,
            style = MaterialTheme.typography.body2,
            modifier = Modifier.padding(horizontal = 16.dp, vertical = 8.dp)
        )
    }
}

@Composable
fun OfflineDialog(onRetry: () -> Unit) {
    AlertDialog(
        onDismissRequest = {},
        title = { Text(text = "stringResource(R.string.connection_error_title)") },
        text = { Text(text = "stringResource(R.string.connection_error_message)") },
        confirmButton = {
            TextButton(onClick = onRetry) {
                Text("stringResource(R.string.retry_label)", color = Red300)
            }
        }
    )
}

@Composable
fun HintDialog(dismissClick: () -> Unit, list: List<String>) {
    //需要自定义时，可以使用
//    Dialog(onDismissRequest = { /*TODO*/ }) {
//
//    }
    AlertDialog(
        modifier = Modifier
            .wrapContentHeight(CenterVertically)
            .wrapContentWidth(CenterHorizontally),
        onDismissRequest = { /*TODO*/ },
        title = {
            Column(
                modifier = Modifier
                    .fillMaxWidth(0.8f)
//                    .wrapContentWidth()
//                    .fillMaxWidth()
//                    .fillMaxHeight(0.4f)
                    .wrapContentHeight(CenterVertically)
                    .background(Red300),
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.CenterHorizontally,
            ) {
                Icon(
                    imageVector = Icons.Default.Warning,
                    tint = Write300,
                    contentDescription = "Warning"
                )
                Text(text = "红外报警", fontSize = 30.sp)
            }
        },
        text = {
            LazyColumn() {
                itemsIndexed(hintTextList) { index, item ->
                    val myItem = if (index < list.size) list[index] else ""
                    Row(modifier = Modifier.padding(bottom = 10.dp)) {
                        Text(
                            text = stringResource(hintTextList[index].first),
                            modifier = Modifier.weight(0.3f),
                            fontSize = 15.sp
                        )
                        Text(
                            text = myItem,
                            modifier = Modifier
                                .weight(0.7f),
//                                .align(Alignment.CenterEnd),
                            textAlign = TextAlign.End,
                            fontSize = 14.sp
                        )
                    }
                }
            }
        },
        confirmButton = {
            Button(
                onClick = { /*TODO*/ },
                colors = ButtonDefaults.buttonColors(
                    backgroundColor = Red300,
                    contentColor = Write300
                )
            ) {
                Text(text = stringResource(id = R.string.check))
            }
        },
        dismissButton = {
            Button(
                onClick = dismissClick,
                colors = ButtonDefaults.buttonColors(
                    backgroundColor = Red300,
                    contentColor = Write300
                )
            ) {
                Text(text = stringResource(id = R.string.confirm))
            }
        }
    )
}


@Suppress("DEPRECATION")
fun checkIfOnline(context: Context): Boolean {
    val cm = context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager

    return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
        val capabilities = cm.getNetworkCapabilities(cm.activeNetwork) ?: return false

        capabilities.hasCapability(NetworkCapabilities.NET_CAPABILITY_INTERNET) &&
                capabilities.hasCapability(NetworkCapabilities.NET_CAPABILITY_VALIDATED)
    } else {
        cm.activeNetworkInfo?.isConnectedOrConnecting == true
    }
}

val automaticList = listOf("11", "一键执行")

//此处绕了个远路
private val hintTextList = listOf(
    R.string.device_name to "",
    R.string.device_event_type to "",
    R.string.family_name to "",
    R.string.occurrence_time to "",
)

@ExperimentalComposeUiApi
@RequiresApi(Build.VERSION_CODES.O)
@InternalCoroutinesApi
@ExperimentalAnimationApi
@ExperimentalFoundationApi
@Preview
@Composable
fun ShowTab() {
    JetsurveyTheme {
        HomeScreenPage(
            rememberNavController(),
            viewModel = viewModel()
        )
    }
}

@Preview
@Composable
fun showDialog() {
    JetsurveyTheme {
        HintDialog({}, list = listOf("", "", "", "yingying"))
    }
}