package com.richmat.mytuya.ui.sign.send_verify_code

import android.util.Log
import androidx.compose.foundation.layout.*
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Menu
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.richmat.mytuya.ui.newHome.Login

@Composable
fun SendVerifyCodeScreen(
    navController: NavController,
    viewModel: SendVerifyCodeViewModel = hiltViewModel(),
) {
    val phone = viewModel.phone.value
    val countryCode = viewModel.countryCode.value

    Scaffold(
        topBar = {
            TopAppBar(navigationIcon = {
                IconButton(
                    onClick = {
                        navController.navigateUp()
                    }
                ) {
                    Icon(
                        imageVector = Icons.Default.ArrowBack,
                        contentDescription = "back"
                    )
                }
            }, actions = {}, title = {})
        }
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = 32.dp)
                .padding(top = 40.dp)
        ) {
            Text(text = "忘记密码", style = MaterialTheme.typography.h6)
            Spacer(modifier = Modifier.height(16.dp))
            Button(onClick = { /*TODO*/ },
                modifier = Modifier.fillMaxWidth(),
                colors = ButtonDefaults.buttonColors(backgroundColor = Color.White)) {
                Text(text = "中国",
                    textAlign = TextAlign.Start,
                    style = MaterialTheme.typography.body1)
                Icon(imageVector = Icons.Default.Menu, contentDescription = null,
                    modifier = Modifier
                        .weight(1f)
                        .wrapContentWidth(Alignment.End))
            }
            Spacer(modifier = Modifier.height(16.dp))
            OutlinedTextField(value = phone,
                textStyle = MaterialTheme.typography.body1,
                modifier = Modifier.fillMaxWidth(),
                onValueChange = {
                    viewModel.onEvent(SendVerifyCodeEvent.EnterPhone(it))
                }, label = {
                    Text(text = "手机号")
                })
            Spacer(modifier = Modifier.height(32.dp))
            Button(onClick = {
                viewModel.onEvent(SendVerifyCodeEvent.SendVerifyCodeWithUserName {
                    Log.e("TAG", "SendVerifyCodeScreen: why 不跳")
                    navController.navigate(Login.ForgetLoginScreen.route)
//                    navController.navigate(Login.ForgetLoginScreen.route)
                })
            },
                modifier = Modifier.fillMaxWidth(),
                enabled = phone.isNotBlank()) {
                Text(text = "获取验证码", style = MaterialTheme.typography.body1)
            }
        }
    }
}