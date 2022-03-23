package com.richmat.mytuya.ui.Home

import android.os.Build
import android.util.Log
import androidx.annotation.RequiresApi
import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.res.stringResource
import androidx.navigation.NavDestination.Companion.hierarchy
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.richmat.mytuya.data.posts.Imp.FakePostsRepository
import com.richmat.mytuya.ui.*
import com.richmat.mytuya.ui.Home.homeViewmoel.dialogViewModel.DialogViewModel
import com.richmat.mytuya.util.data.TAG
import com.tuya.smart.sdk.bean.DeviceBean
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.DelicateCoroutinesApi
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.InternalCoroutinesApi
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.stateIn
import java.time.LocalDateTime

internal const val duration = 500
internal const val offset = 1000

@ExperimentalMaterialApi
@ExperimentalComposeUiApi
@DelicateCoroutinesApi
@RequiresApi(Build.VERSION_CODES.O)
@InternalCoroutinesApi
@ExperimentalFoundationApi
@ExperimentalAnimationApi
@Composable
fun HomeScreen(
    startPage: Page,
    viewModel: DialogViewModel = androidx.lifecycle.viewmodel.compose.viewModel(),
) {
//    val navController = rememberAnimatedNavController()
    val navController = rememberNavController()
    val tableItems = listOf(TabItem.HomeTab, TabItem.SmartTab, TabItem.MyselfTab)
    val isShow by viewModel.isShowWarning.collectAsState()
    val globalDialog1 =
        FakePostsRepository().observeIsShowDialog().stateIn(
            CoroutineScope(Dispatchers.IO),
            SharingStarted.WhileSubscribed(5000),
            false
        )
    val selectedTopics by globalDialog1.collectAsState()
    Log.e(TAG, "HomeScreen: selectedTopics ${selectedTopics},isShow: $isShow")
    Scaffold(
        bottomBar = {
            val navBackStackEntry by navController.currentBackStackEntryAsState()
            val currentRote = navBackStackEntry?.destination
//                if (currentRote?.route in tableItems)

            val needShowBottom = when (currentRote?.route) {
                TabItem.HomeTab.page.route, TabItem.SmartTab.page.route, TabItem.MyselfTab.page.route -> {
                    true
                }
                else -> {
                    false
                }
            }
            if (needShowBottom) {
                BottomNavigation() {
                    tableItems.forEach { tabItem ->
                        BottomNavigationItem(
                            icon = { Icon(tabItem.iconImage, "") },
                            label = { Text(text = stringResource(id = tabItem.resourceId)) },
                            selected = currentRote?.hierarchy?.any { it.route == tabItem.page.route } == true,
                            onClick = {
                                navController.navigate(tabItem.page.route) {
                                    //返回刷新
//                                    navController.popBackStack()
                                    popUpTo(navController.graph.findStartDestination().id) {
//                                        官方例子，没有使用此项
//                                        inclusive = true
                                        saveState = true
                                    }
                                    launchSingleTop = true
                                    restoreState = true
                                }
                            })
                    }
                }
            }
        }
    ) {
        AnimationHomeNavHost(navController = navController, startPage)
    }

    if (isShow) {
        HintDialog(
            { FakePostsRepository().resetDeviceMemory(DeviceBean()) },
            listOf(
                "name",
                "hongwai",
                "Rich",
                System.currentTimeMillis().toString() + LocalDateTime.now().toString()
            )
        )
    }
}

@ExperimentalMaterialApi
@ExperimentalComposeUiApi
@RequiresApi(Build.VERSION_CODES.O)
@InternalCoroutinesApi
@ExperimentalFoundationApi
@ExperimentalAnimationApi
@Composable
fun AnimationHomeNavHost(navController: NavHostController, startPage: Page) {
    NavHost(navController = navController, startDestination = startPage.route) {
//    AnimatedNavHost(navController = navController, startDestination = startPage.route) {
        composableHome(navController)
        composableSmart(navController)
        composableMyself(navController)
        composableSearchDevice(navController)
//TODO 使用嵌套图优化
        composableSignIn(navController)
        composableLogin(navController)
        composableSignUp(navController)
        composableSetPassword(navController)
        composableVerificationCode(navController)
        composableSetting(navController)
        composableGateWay(navController)
        composableSearchChildDevice(navController)
        composableBodySensor(navController)
        composableSelectDevice(navController)
        composableSearchResultScreen(navController)
        composableDevSetting(navController)
        composableBulbLight(navController)
        composableLoginScreen(navController)
        composableSendVerifyCodeScreen(navController)
        composableForgetLoginScreen(navController)
        composableSelectCountryScreen(navController)
        composableRegisterScreen(navController)
        composableVerifyRegisterCodeScreen(navController)
        composableSetAccountPasswordScreen(navController)
    }
}

