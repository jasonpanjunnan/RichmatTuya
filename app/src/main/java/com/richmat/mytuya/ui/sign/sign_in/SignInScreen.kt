package com.richmat.mytuya.ui.sign.sign_in

import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ExpandMore
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Alignment.Companion.CenterHorizontally
import androidx.compose.ui.Alignment.Companion.End
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import com.example.compose.jetsurvey.theme.Orange800
import com.richmat.mytuya.R
import com.richmat.mytuya.ui.Home.Login
import com.richmat.mytuya.ui.Home.TabItem
import com.richmat.mytuya.util.getCountryCode
import com.richmat.mytuya.util.getCountryName
import com.richmat.mytuya.util.supportWideScreen
import kotlinx.coroutines.launch

@ExperimentalMaterialApi
@Composable
fun SignInScreen(
    navController: NavHostController,
    viewModel: SignInViewModel = hiltViewModel(),
) {
    val country by viewModel.currentCountry.collectAsState()
    val sheetState = rememberModalBottomSheetState(ModalBottomSheetValue.Hidden)
    val scope = rememberCoroutineScope()

    ModalBottomSheetLayout(sheetState = sheetState,
        sheetContent = {
            Column(modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = 32.dp),
                horizontalAlignment = Alignment.CenterHorizontally) {
                TextButton(onClick = {
                    navController.navigate(Login.SelectCountryScreen.route)
                }, modifier = Modifier.align(End)) {
                    Text(text = country.getCountryName(), style = MaterialTheme.typography.body2)
                    Icon(imageVector = Icons.Default.ExpandMore, contentDescription = null)
                }
                Spacer(modifier = Modifier.height(16.dp))
                Text(
                    text = "没有账号，将无法使用App中的部分功能",
                    style = MaterialTheme.typography.body1,
//                    modifier = Modifier.fillMaxWidth().wrapContentWidth(CenterHorizontally)
                )
                Spacer(modifier = Modifier.height(32.dp))
                Button(onClick = {
                    viewModel.onEvent(SignInEvent.TouristRegisterAndLogin(country.getCountryCode()) {
                        navController.navigate((TabItem.HomeTab.page.route)) {
                            //清空顶上的回收栈
//                            navController.popBackStack()
                            popUpTo(navController.graph.findStartDestination().id) {
                                //此项可控制是否退出首项 StartDestination
//                                inclusive = true
                                saveState = true
                            }
                            launchSingleTop = true
                            restoreState = true
                        }
                    })
                }) {
                    Text(text = "继续体验", modifier = Modifier
                        .fillMaxWidth(0.7f)
                        .wrapContentWidth(CenterHorizontally),
                        style = MaterialTheme.typography.body1
                    )
                }
            }
        }) {
        Surface(modifier = Modifier.fillMaxSize()) {
//        Image(painter = painterResource(id = R.drawable.starry_sky),
//            contentDescription = null,
//            modifier = Modifier.fillMaxSize(),)
            Column() {
                Branding(
                    Modifier
                        .padding(horizontal = 76.dp)
                        .align(Alignment.CenterHorizontally)
                        .wrapContentHeight(align = Alignment.CenterVertically)
                        .weight(1f)
                )
                SignInAndCreateAccount(
                    modifier = Modifier
                        .supportWideScreen()
                        .padding(40.dp),
                    showSheet = { scope.launch { sheetState.show() } },
                    navController
                )
            }
        }
    }
}

@Composable
fun SignInAndCreateAccount(
    modifier: Modifier = Modifier,
    showSheet: () -> Unit, navController: NavHostController,
) {
    Column(
        modifier = modifier,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        LongButton(
            "登录",
            onSubmit = {
                navController.navigate(Login.LoginScreen.route) {
//                navController.navigate(Page.Login.route) {
                    //TODO 可以处理一些栈信息,下面同理
                }
            },
            color = ButtonDefaults.buttonColors(
                backgroundColor = Orange800
            ),
            modifier = Modifier
        )
        Spacer(modifier = Modifier.height(20.dp))
        LongButton("注册", onSubmit = {
            navController.navigate(Login.RegisterScreen.route) {
            }
        })
        TextButton(onClick = showSheet) {
            Text(
                text = "游客登陆",
                modifier = Modifier.padding(top = 20.dp, bottom = 20.dp),
                color = Color.Gray
            )
        }
    }
}

@Composable
fun Branding(modifier: Modifier = Modifier) {
    Image(
        painter = painterResource(R.mipmap.ic_launcher),
        contentDescription = null,
        modifier = modifier.size(145.dp)
    )
}

@Composable
fun LongButton(
    str: String,
    modifier: Modifier = Modifier,
    onSubmit: () -> Unit,
    color: ButtonColors = ButtonDefaults.buttonColors(),
) {
    Button(
        onClick = onSubmit,
        modifier = modifier
            .fillMaxWidth()
            .padding(horizontal = 32.dp)
            .height(48.dp),
//        modifier = modifier,
        colors = color
    ) {
        Text(text = str, style = MaterialTheme.typography.subtitle2, fontSize = 17.sp)
    }
}

@ExperimentalMaterialApi
@ExperimentalAnimationApi
@Preview
@Composable
fun ShowScreen() {
    SignInScreen(navController = rememberNavController())
}