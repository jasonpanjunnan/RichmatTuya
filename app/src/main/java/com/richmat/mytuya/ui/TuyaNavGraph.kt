package com.richmat.mytuya.ui

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.platform.LocalContext
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.Lifecycle
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.composable
import androidx.navigation.compose.navArgument
import com.richmat.mytuya.mNeedBackGesture
import com.richmat.mytuya.ui.Home.*
import com.richmat.mytuya.ui.Home.homeViewmoel.HomeViewModel
import com.richmat.mytuya.ui.device.gateway.ChildSearchViewModel
import com.richmat.mytuya.ui.device.gateway.ChildSearchViewModel.Companion.SEARCH_CHILD_KEY
import com.richmat.mytuya.ui.device.gateway.GateWayScreen
import com.richmat.mytuya.ui.device.gateway.GatewayViewModel
import com.richmat.mytuya.ui.device.gateway.GatewayViewModel.Companion.GATEWAY_CHILD_KEY
import com.richmat.mytuya.ui.device.gateway.SearchDeviceChildScreen
import com.richmat.mytuya.ui.deviceSetting.DevSetting
import com.richmat.mytuya.ui.deviceSetting.DevSettingViewModel
import com.richmat.mytuya.ui.deviceSurface.BodySensorScreen
import com.richmat.mytuya.ui.deviceSurface.BulbLight
import com.richmat.mytuya.ui.deviceSurface.viewModel.BodySensorViewModel
import com.richmat.mytuya.ui.deviceSurface.viewModel.BulbLightViewModel
import com.richmat.mytuya.ui.searchDevice.SearchDeviceScreen
import com.richmat.mytuya.ui.searchDevice.SearchDeviceViewModel
import com.richmat.mytuya.ui.searchDevice.SelectDeviceScreen
import com.richmat.mytuya.ui.searchDevice.SelectDeviceViewModel
import com.richmat.mytuya.ui.searchDevice.SelectDeviceViewModel.Companion.HOME_ID
import com.richmat.mytuya.ui.searchResult.SearchResultScreen
import com.richmat.mytuya.ui.searchResult.SearchResultViewModel
import com.richmat.mytuya.ui.setting.SettingScreen
import com.richmat.mytuya.ui.sign.*
import com.richmat.mytuya.ui.sign.forget_password_login.ForgetLoginScreen
import com.richmat.mytuya.ui.sign.register.RegisterScreen
import com.richmat.mytuya.ui.sign.register.compoments.SelectCountryScreen
import com.richmat.mytuya.ui.sign.send_verify_code.SendVerifyCodeScreen
import com.richmat.mytuya.ui.sign.set_account_password.SetAccountPasswordScreen
import com.richmat.mytuya.ui.sign.sign_in.SignInScreen
import com.richmat.mytuya.ui.sign.verify_register_code.VerifyRegisterCodeScreen
import com.richmat.mytuya.ui.theme.JetsurveyTheme
import kotlinx.coroutines.InternalCoroutinesApi

//TODO 要把viewmodel提出来，使用uiStates传入。10.28 没必要，传状态不好找，再封一层即可
@ExperimentalMaterialApi
@InternalCoroutinesApi
@RequiresApi(Build.VERSION_CODES.O)
@ExperimentalAnimationApi
fun NavGraphBuilder.composableSearchDevice(navController: NavHostController) {
    composable(
        route = "${Page.SearchDevice.route}/{$HOME_ID}",
        arguments = listOf(navArgument(HOME_ID) { type = NavType.LongType })
    ) { backStackEntry ->
        //TODO 注：无法使用by
        val viewModel: SearchDeviceViewModel = hiltViewModel(backStackEntry)
//            viewModel(
//            factory = SearchDeviceViewModel.provideFactory(
//                owner = backStackEntry,
//                deaultArgs = backStackEntry.arguments,
//                connectRepository = Connect(),
//                myRepository = myFakePostsRepository
////                        myRepository = FakePostsRepository()
//            )
//        )
        SearchDeviceScreen(navController, viewModel)
    }
}

@ExperimentalFoundationApi
@ExperimentalAnimationApi
fun NavGraphBuilder.composableMyself(navController: NavHostController) {
    composable(
        Page.Myself.route,
//        "${Page.Myself.route}/{version}",
//        arguments = listOf(
//            navArgument("version") {
//                type = NavType.StringType
//                defaultValue = "1.23"
//            }
//        ),
//        exitTransition = { _, target ->
//            when (target.destination.route) {
//                Page.Smart.route, Page.Home.route ->
//                    slideOutOfContainer(
//                        towards = AnimatedContentScope.SlideDirection.Right,
//                        targetOffset = { offset },
//                        animationSpec = tween(duration)
//                    ) + fadeOut(animationSpec = tween(duration))
//                else -> null
//            }
//        },
//        enterTransition = { initial, _ ->
//            when (initial.destination.route) {
//                Page.Smart.route, Page.Home.route -> {
//                    slideIntoContainer(
//                        towards = AnimatedContentScope.SlideDirection.Right,
//                        initialOffset = { offset },
//                        animationSpec = tween(duration)
//                    ) + fadeIn(animationSpec = tween(duration))
//                }
//                else -> null
//            }
//        },
//        popEnterTransition = { initial, _ ->
//            when (initial.destination.route) {
//                Page.Smart.route , Page.Home.route-> {
//                    slideIntoContainer(
//                        towards = AnimatedContentScope.SlideDirection.Left,
//                        initialOffset = { -offset },
//                        animationSpec = tween(duration)
//                    ) + fadeIn(animationSpec = tween(duration))
//                }
//                else -> null
//            }
//        }
    ) { backStackEntry ->
//        val version = backStackEntry.arguments?.getString("version") ?: ""
//        PersonalScreenPage(version)
        PersonalScreenPage(navController)
    }
}

@ExperimentalAnimationApi
fun NavGraphBuilder.composableSmart(navController: NavHostController) {
    composable(
        Page.Smart.route,
//        exitTransition = { _, target ->
//            when (target.destination.route) {
//                Page.Myself.route ->
//                    slideOutOfContainer(
//                        towards = AnimatedContentScope.SlideDirection.Right,
//                        targetOffset = { -offset },
//                        animationSpec = tween(duration)
//                    ) + fadeOut(animationSpec = tween(duration))
//                Page.Home.route ->
//                    slideOutOfContainer(
//                        towards = AnimatedContentScope.SlideDirection.Right,
//                        targetOffset = { offset },
//                        animationSpec = tween(duration)
//                    ) + fadeOut(animationSpec = tween(duration))
//                else -> null
//            }
//        },
//        enterTransition = { initial, _ ->
//            when (initial.destination.route) {
//                Page.Myself.route -> {
//                    slideIntoContainer(
//                        towards = AnimatedContentScope.SlideDirection.Right,
//                        initialOffset = { -offset },
//                        animationSpec = tween(duration)
//                    ) + fadeIn(animationSpec = tween(duration))
//                }
//                Page.Home.route -> {
//                    slideIntoContainer(
//                        towards = AnimatedContentScope.SlideDirection.Right,
//                        initialOffset = { offset },
//                        animationSpec = tween(duration)
//                    ) + fadeIn(animationSpec = tween(duration))
//                }
//                else -> null
//            }
//        }
    ) {
        SmartScreenPage(navController)
    }
}

@ExperimentalComposeUiApi
@RequiresApi(Build.VERSION_CODES.O)
@InternalCoroutinesApi
@ExperimentalFoundationApi
@ExperimentalAnimationApi
fun NavGraphBuilder.composableHome(navController: NavHostController) {
    composable(
        Page.Home.route,
//        exitTransition = { _, target ->
//            when (target.destination.route) {
//                Page.Smart.route, Page.Myself.route ->
//                    slideOutOfContainer(
//                        towards = AnimatedContentScope.SlideDirection.Left,
//                        targetOffset = { -offset },
//                        animationSpec = tween(duration)
//                    ) + fadeOut(animationSpec = tween(duration))
//                else -> null
//            }
//        },
//        enterTransition = { initial, _ ->
//            when (initial.destination.route) {
//                Page.Smart.route, Page.Myself.route -> {
//                    slideIntoContainer(
//                        towards = AnimatedContentScope.SlideDirection.Left,
//                        initialOffset = { -offset },
//                        animationSpec = tween(duration)
//                    ) + fadeIn(animationSpec = tween(duration))
//                }
//                else -> null
//            }
//        }
    ) { backStackEntry ->
        //检测网络状态
        val context = LocalContext.current
        var isOnline by remember { mutableStateOf(checkIfOnline(context)) }

        // TODO: add some navigation
        if (isOnline) {
//            val homeViewModel: HomeViewModel = viewModel(
//                //TODO 从这开始
//                factory = HomeViewModel.provideFactory(
//                    postsRepository = myFakePostsRepository,
////                            postsRepository = FakePostsRepository(),
//                    owner = backStackEntry,
//                    deaultArgs = backStackEntry.arguments
//                )
//            )
            val homeViewModel: HomeViewModel = hiltViewModel()
            HomeScreenPage(navController, viewModel = homeViewModel)
        } else {
            OfflineDialog { isOnline = checkIfOnline(context) }
        }
    }
}

@ExperimentalMaterialApi
@ExperimentalAnimationApi
fun NavGraphBuilder.composableSignIn(navController: NavHostController) {
    composable(Page.SignIn.route) {
        SignInScreen(navController)
    }
}

@ExperimentalAnimationApi
fun NavGraphBuilder.composableLogin(navController: NavHostController) {
    composable(Page.Login.route) {
        JetsurveyTheme {
            SecondSignInScreen(navController)
        }
    }
}

@ExperimentalAnimationApi
fun NavGraphBuilder.composableSignUp(navController: NavHostController) {
    composable(Page.SignUp.route) {
        NewSignUpScreen(navController)
    }
}

@ExperimentalAnimationApi
fun NavGraphBuilder.composableSetPassword(navController: NavHostController) {
    composable(Page.SetPassword.route) {
        SetPasswordScreen(navController)
    }
}

@ExperimentalAnimationApi
fun NavGraphBuilder.composableVerificationCode(navController: NavHostController) {
    composable(Page.VerificationCode.route) {
        VerificationCodeScreen(navController)
    }
}

@ExperimentalAnimationApi
fun NavGraphBuilder.composableSetting(navController: NavHostController) {
    composable(Page.Setting.route) {
        SettingScreen(navController)
    }
}

//@ExperimentalAnimationApi
//fun NavGraphBuilder.composableSearchChildDevice(navController: NavHostController) {
//    composable(DevicePage.SearchChildDevice.route) {
//        SearchDeviceChildScreen(navController)
//    }
//}

@ExperimentalAnimationApi
fun NavGraphBuilder.composableSearchChildDevice(navController: NavHostController) {
    composable(
        "${DevicePage.SearchChildDevice.route}/{$SEARCH_CHILD_KEY}",
        arguments = listOf(navArgument(SEARCH_CHILD_KEY) { type = NavType.StringType })
    ) { backStackEntry ->
        val viewModel: ChildSearchViewModel = hiltViewModel(backStackEntry)
//            viewModel(
//            factory = provideFactory(
//                searchRepository = ChildSearchRepository(),
//                owner = backStackEntry,
//                default = backStackEntry.arguments
//            )
//        )
        SearchDeviceChildScreen(navController, viewModel)
    }
}

/**
 * navigate都先改成可选参数了，不好啊。暂时先这样，肯定不好，后边需求有了再改，并不麻烦
 */
@ExperimentalFoundationApi
@ExperimentalAnimationApi
fun NavGraphBuilder.composableGateWay(navController: NavHostController) {
    composable(
        "${DevicePage.Gateway.route}?${GATEWAY_CHILD_KEY}={$GATEWAY_CHILD_KEY}",
//        "${DevicePage.Gateway.route}/{$GATEWAY_CHILD_KEY}",
        arguments = listOf(navArgument(GATEWAY_CHILD_KEY) { defaultValue = "me" })
//        arguments = listOf(navArgument(GATEWAY_CHILD_KEY) { type = NavType.StringType })
    ) { backStackEntry ->
        val viewModel: GatewayViewModel = hiltViewModel(backStackEntry)
        GateWayScreen(navController, viewModel)
    }
}

@ExperimentalFoundationApi
@ExperimentalAnimationApi
fun NavGraphBuilder.composableBodySensor(navController: NavHostController) {
    composable(
        "${DevicePage.BodySensor.route}?${BodySensorViewModel.DEVICE_ID}={${BodySensorViewModel.DEVICE_ID}}",
        arguments = listOf(navArgument(BodySensorViewModel.DEVICE_ID) { defaultValue = "me" })
//        arguments = listOf(navArgument(BodySensorViewModel.DEVICE_ID) { type = NavType.StringType })
    ) { backStackEntry ->
        val viewModel: BodySensorViewModel = hiltViewModel(backStackEntry)
        BodySensorScreen(navController, viewModel)
    }
}

@RequiresApi(Build.VERSION_CODES.N)
@ExperimentalFoundationApi
@ExperimentalAnimationApi
fun NavGraphBuilder.composableBulbLight(navController: NavHostController) {
    composable(
////        route = DevicePage.BulbLight.route,
//        "${DevicePage.BulbLight.route}?{${BodySensorViewModel.DEVICE_ID} = ${BodySensorViewModel.DEVICE_ID}}",
////        "${DevicePage.BulbLight.route}/{${BodySensorViewModel.DEVICE_ID}}",
//        arguments = listOf(navArgument(BodySensorViewModel.DEVICE_ID) {
//        defaultValue = "me"})
        "${DevicePage.BulbLight.route}?userId={userId}",
        arguments = listOf(navArgument("userId") { defaultValue = "me" })
    ) { backStackEntry ->
        val viewModel: BulbLightViewModel = hiltViewModel(backStackEntry)
        BulbLight(back = { navController.navigateUp() }, viewModel = viewModel, setting = {})
    }
}

@ExperimentalFoundationApi
@ExperimentalAnimationApi
fun NavGraphBuilder.composableSelectDevice(navController: NavHostController) {
    composable(
        route = "${Page.SelectDevice.route}/{$HOME_ID}",
        arguments = listOf(navArgument(HOME_ID) { type = NavType.LongType })
    ) { backStackEntry ->
        mNeedBackGesture = true
        if (backStackEntry.lifecycle.currentState == Lifecycle.State.CREATED) {
            mNeedBackGesture = false
        }
        val viewModel: SelectDeviceViewModel = hiltViewModel(backStackEntry)
        SelectDeviceScreen(navController, viewModel)
    }
}

@ExperimentalFoundationApi
@ExperimentalAnimationApi
fun NavGraphBuilder.composableSearchResultScreen(navController: NavHostController) {
    composable(
        route = "${Page.SearchUiState.route}/{${SearchResultViewModel.DEV_RESULT}}",
        //TODO 无法传序列化对象，先传一个名字
//        arguments = listOf(navArgument(SearchResultViewModel.DEV_RESULT) {
//            type = NavType.ParcelableType<DevResultMassage>(type = DevResultMassage::class.java)
//        })
        arguments = listOf(navArgument(SearchResultViewModel.DEV_RESULT) {
            type = NavType.StringType
        })
    ) { backStackEntry ->
        val viewModel: SearchResultViewModel = hiltViewModel(backStackEntry)
        SearchResultScreen(complete = { route ->
            //TODO 测试用的，因为没搜到，不会走到注册的地方。记得改回去，没有值会死，注意判空
//            navController.navigate("${DevicePage.BodySensor.route}/${"11111112222222"}")
            navController.navigate(route)
            {
                popUpTo(navController.graph.findStartDestination().id) {
                    //此项可控制是否退出首项 StartDestination
//                    inclusive = true
                    //这个会保存popupto之上的路径状态
//                    saveState = true
                }
                launchSingleTop = true
                restoreState = true
            }
        }, viewModel = viewModel)
    }
}

@ExperimentalAnimationApi
fun NavGraphBuilder.composableDevSetting(navController: NavHostController) {
    composable(
        "${Page.DevSetting.route}/{${DevSettingViewModel.DEV_INFO}}",
        arguments = listOf(navArgument(DevSettingViewModel.DEV_INFO) { type = NavType.StringType })
    ) { backStackEntry ->
        val viewModel: DevSettingViewModel = hiltViewModel(backStackEntry)
        DevSetting(viewModel, onBack = { navController.navigateUp() })
    }
}

@ExperimentalAnimationApi
fun NavGraphBuilder.composableLoginScreen(navController: NavHostController) {
    composable(
        route = Login.LoginScreen.route,
//        "${Page.DevSetting.route}/{${DevSettingViewModel.DEV_INFO}}",
//        arguments = listOf(navArgument(DevSettingViewModel.DEV_INFO) { type = NavType.StringType })
    ) {
        LoginScreen(navigation = navController)
    }
}

@ExperimentalAnimationApi
fun NavGraphBuilder.composableSendVerifyCodeScreen(navController: NavHostController) {
    composable(route = Login.SendVerifyCodeScreen.route) {
        SendVerifyCodeScreen(navController = navController)
    }
}

@ExperimentalAnimationApi
fun NavGraphBuilder.composableForgetLoginScreen(navController: NavHostController) {
    composable(
        //必须要的可以使用“//”,可选的最好加判断
        route = Login.ForgetLoginScreen.route + "?countryCode={countryCode}&phone={phone}",
        arguments = listOf(navArgument(name = "countryCode") {
            type = NavType.StringType
            defaultValue = "86"
        }, navArgument(name = "phone") {
            type = NavType.StringType
            defaultValue = "13666666666"
        })
    ) { backStackEntry ->
        ForgetLoginScreen(navController = navController, viewModel = hiltViewModel(backStackEntry))
    }
}

@ExperimentalAnimationApi
fun NavGraphBuilder.composableSelectCountryScreen(navController: NavHostController) {
    composable(route = Login.SelectCountryScreen.route) {
        SelectCountryScreen(navController = navController)
    }
}

@ExperimentalAnimationApi
fun NavGraphBuilder.composableRegisterScreen(navController: NavHostController) {
    composable(route = Login.RegisterScreen.route) {
        RegisterScreen(navController = navController)
    }
}

@ExperimentalAnimationApi
fun NavGraphBuilder.composableVerifyRegisterCodeScreen(navController: NavHostController) {
    composable(
        //必须要的可以使用“//”,可选的最好加判断
        route = Login.VerifyRegisterCodeScreen.route + "?countryCode={countryCode}&phone={phone}",
        arguments = listOf(navArgument(name = "countryCode") {
            type = NavType.StringType
            defaultValue = "86"
        }, navArgument(name = "phone") {
            type = NavType.StringType
            defaultValue = "13666666666"
        })
    ) { backStackEntry ->
        VerifyRegisterCodeScreen(navController = navController,
            viewModel = hiltViewModel(backStackEntry))
    }
}

@ExperimentalAnimationApi
fun NavGraphBuilder.composableSetAccountPasswordScreen(navController: NavHostController) {
    composable(
        //必须要的可以使用“//”,可选的最好加判断
        route = Login.SetAccountPasswordScreen.route + "?countryCode={countryCode}&phone={phone}&verify_code={verify_code}",
        arguments = listOf(navArgument(name = "countryCode") {
            type = NavType.StringType
            defaultValue = "86"
        }, navArgument(name = "phone") {
            type = NavType.StringType
            defaultValue = "13666666666"
        }, navArgument(name = "verify_code") {
            type = NavType.StringType
            defaultValue = "666666"
        })
    ) { backStackEntry ->
        SetAccountPasswordScreen(navController = navController,
            viewModel = hiltViewModel(backStackEntry))
    }
}