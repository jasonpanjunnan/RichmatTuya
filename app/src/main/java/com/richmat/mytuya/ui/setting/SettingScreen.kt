package com.richmat.mytuya.ui.setting

import android.widget.Toast
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.Button
import androidx.compose.material.Scaffold
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.NavHostController
import com.richmat.mytuya.ui.components.SignInSignUpTopAppBar
import com.example.compose.jetsurvey.theme.Red800
import com.richmat.mytuya.MyApplication
import com.richmat.mytuya.R
import com.richmat.mytuya.ui.Home.Page
import com.tuya.smart.android.user.api.ILogoutCallback
import com.tuya.smart.home.sdk.TuyaHomeSdk

@Composable
fun SettingScreen(navController: NavHostController) {
    Scaffold(
        topBar = {
            SignInSignUpTopAppBar(
                topAppBarText = stringResource(id = R.string.set_setting),
                onBackPressed = { navController.navigateUp() }
            )
        },
        content = {
            LazyColumn(modifier = Modifier.fillMaxWidth()) {
                item {
                    Button(
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(40.dp),
                        onClick = {
//                            退出登录，函数
                            TuyaHomeSdk.getUserInstance().logout(object : ILogoutCallback {
                                override fun onSuccess() {
                                    Toast.makeText(
                                        MyApplication.context,
                                        "退出登录成功",
                                        Toast.LENGTH_SHORT
                                    ).show()
                                    //退出登录成功
                                    navController.navigate(Page.SignIn.route) {
                                        //TODO ????? 为啥要执行两次才行
//                                        navController.popBackStack(Page.SignIn.route,true)
//                                        navController.navigateUp()
//                                        navController.popBackStack()
                                        //暂时无用
                                        popUpTo(navController.graph.findStartDestination().id) {
//                                            //此项可控制是否退出首项 StartDestination
//                                            inclusive = true
//                                            saveState = true
                                        }
                                        launchSingleTop = true
                                        restoreState = true
                                    }
                                }

                                override fun onError(errorCode: String, errorMsg: String) {
                                    Toast.makeText(
                                        MyApplication.context,
                                        "code: " + errorCode + "error:" + errorMsg + "退出登录失败",
                                        Toast.LENGTH_SHORT
                                    ).show()
                                }
                            })
                        }) {
                        Text(text = "退出登陆", color = Red800)
                    }
                }
            }
        }
    )
}