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
import androidx.navigation.NavHostController
import com.example.compose.jetsurvey.theme.Orange800
import com.google.accompanist.navigation.animation.rememberAnimatedNavController
import com.richmat.mytuya.R
import com.richmat.mytuya.ui.newHome.Page
import com.richmat.mytuya.util.supportWideScreen

@Composable
fun SignInScreen(navController: NavHostController) {
    Surface(modifier = Modifier.supportWideScreen()) {
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
                    .padding(20.dp),
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
                navController.navigate(Page.Login.route) {
                    //TODO 可以处理一些栈信息,下面同理
                }
            },
            color = ButtonDefaults.buttonColors(
                backgroundColor = Orange800
            ),
            modifier = modifier
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
        modifier = modifier
    )
}

@Composable
fun LongButton(
    str: String,
    modifier: Modifier = Modifier,
    onSubmit: () -> Unit,
    color: ButtonColors = ButtonDefaults.buttonColors()
) {
    Button(
        onClick = onSubmit,
        modifier = Modifier.fillMaxWidth(),
//        modifier = modifier,
        colors = color
    ) {
        Text(text = str, style = MaterialTheme.typography.subtitle2)
    }
}

@ExperimentalAnimationApi
@Preview
@Composable
fun ShowScreen() {
    SignInScreen(navController = rememberAnimatedNavController())
}