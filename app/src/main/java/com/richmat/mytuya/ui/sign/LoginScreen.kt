package com.richmat.mytuya.ui.sign

import androidx.compose.foundation.layout.*
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Menu
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment.Companion.End
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import androidx.navigation.NavGraph.Companion.findStartDestination
import com.richmat.mytuya.ui.newHome.TabItem

@Composable
fun LoginScreen(
    navigation: NavController,
    viewModel: LoginViewModel = hiltViewModel(),
) {
    val phone = viewModel.phone.value
    val password = viewModel.password.value
    val countryCode = viewModel.countryCode.value

    Scaffold(
        topBar = {
            TopAppBar(navigationIcon = {
                IconButton(
                    onClick = {
                        navigation.navigateUp()
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
            Text(text = "登录", style = MaterialTheme.typography.h6)
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
                        .wrapContentWidth(End))
            }
            Spacer(modifier = Modifier.height(16.dp))
            OutlinedTextField(value = phone,
                textStyle = MaterialTheme.typography.body1,
                modifier = Modifier.fillMaxWidth(),
                onValueChange = {
                    viewModel.onLoginEvent(LoginEvent.EnterPhone(it))
                }, label = {
                    Text(text = "手机号")
                })
            Spacer(modifier = Modifier.height(16.dp))
            OutlinedTextField(modifier = Modifier.fillMaxWidth(), value = password,
                textStyle = MaterialTheme.typography.body1, onValueChange = {
                    viewModel.onLoginEvent(LoginEvent.EnterPassword(it))
                }, label = {
                    Text(text = "密码")
                })
            Spacer(modifier = Modifier.height(32.dp))
            Button(onClick = {
                viewModel.onLoginEvent(LoginEvent.Login {
                    navigation.navigate((TabItem.HomeTab.page.route)) {
                        //清空顶上的回收栈
                        navigation.popBackStack()
                        popUpTo(navigation.graph.findStartDestination().id) {
                            //此项可控制是否退出首项 StartDestination
                            inclusive = true
                            saveState = true
                        }
                        launchSingleTop = true
                        restoreState = true
                    }
                })
            },
                modifier = Modifier.fillMaxWidth(),
                enabled = phone.isNotBlank() && password.isNotBlank()) {
                Text(text = "登录", style = MaterialTheme.typography.body1)
            }
            Spacer(modifier = Modifier.height(32.dp))
            TextButton(onClick = {
//                navigation.navigate("")
            }, modifier = Modifier.fillMaxWidth()) {
                Text(text = "忘记密码", style = MaterialTheme.typography.body1, color = Color.Blue)
            }
        }
    }
}