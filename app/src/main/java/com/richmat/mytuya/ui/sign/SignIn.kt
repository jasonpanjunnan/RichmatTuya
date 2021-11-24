package com.richmat.mytuya.ui.sign

import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import com.example.compose.jetsurvey.theme.Orange800
import com.richmat.mytuya.R
import com.richmat.mytuya.ui.newHome.Login
import com.richmat.mytuya.ui.newHome.Page
import com.richmat.mytuya.util.supportWideScreen

@Composable
fun SignInScreen(navController: NavHostController) {
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
                navController
            )
        }
    }
}

@Composable
fun SignInAndCreateAccount(modifier: Modifier = Modifier, navController: NavHostController) {
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
            navController.navigate(Page.SignUp.route) {
            }
        })
        Text(
            text = "游客登陆",
            modifier = Modifier.padding(top = 20.dp, bottom = 20.dp),
            color = Color.Gray
        )
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

@ExperimentalAnimationApi
@Preview
@Composable
fun ShowScreen() {
    SignInScreen(navController = rememberNavController())
}