package com.richmat.mytuya.ui.sign.set_account_password

import androidx.compose.foundation.layout.*
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import androidx.navigation.NavGraph.Companion.findStartDestination
import com.richmat.mytuya.ui.newHome.TabItem

@Composable
fun SetAccountPasswordScreen(
    navController: NavController,
    viewModel: SetAccountPasswordViewModel = hiltViewModel(),
) {
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
            var password by remember {
                mutableStateOf("")
            }
            Text(text = "设置密码", style = MaterialTheme.typography.h5)
            Spacer(modifier = Modifier.height(16.dp))
            Spacer(modifier = Modifier.height(16.dp))
            OutlinedTextField(value = password,
                textStyle = MaterialTheme.typography.body1,
                modifier = Modifier.fillMaxWidth(),
                onValueChange = {
                    password = it
                }, label = {
                    Text(text = "密码")
                })
            Spacer(modifier = Modifier.height(10.dp))
            Text(text = "请输入至少6位数字或字母",
                style = MaterialTheme.typography.body2,
                color = MaterialTheme.colors.primary)
            Spacer(modifier = Modifier.height(22.dp))
            Button(onClick = {
                viewModel.register(password) {
                    navController.navigate((TabItem.HomeTab.page.route)) {
                        popUpTo(navController.graph.findStartDestination().id) {
                            //此项可控制是否退出首项 StartDestination
//                            inclusive = true
                            saveState = true
                        }
                        launchSingleTop = true
                        restoreState = true
                    }
                }
            },
                modifier = Modifier.fillMaxWidth(),
                enabled = password.isNotBlank() && !password.contains(" ") && password.length >= 6) {
                Text(text = "完成", style = MaterialTheme.typography.body1)
            }
        }
    }
}