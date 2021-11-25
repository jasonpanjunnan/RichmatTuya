package com.richmat.mytuya.ui.sign.verify_register_code

import androidx.compose.foundation.layout.*
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.richmat.mytuya.ui.Home.Login
import com.richmat.mytuya.ui.sign.forget_password_login.compoments.SimpleVerificationCodeItem
import com.richmat.mytuya.ui.sign.forget_password_login.compoments.VerificationCodeField

@Composable
fun VerifyRegisterCodeScreen(
    navController: NavController,
    viewModel: VerifyRegisterCodeViewModel = hiltViewModel(),
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
            Text(text = "输入验证码", style = MaterialTheme.typography.h6)
            Spacer(modifier = Modifier.height(32.dp))
            VerificationCodeField(6,
                inputCallback = {
                    navController.navigate(
                        Login.SetAccountPasswordScreen.route +
                                "?countryCode=${viewModel.countryCode}&phone=${viewModel.phone}&verify_code=${it}"
                    )
                }) { text, focused ->
                SimpleVerificationCodeItem(text, focused)
            }
            Spacer(modifier = Modifier.height(32.dp))
            Text(text = "验证码已发送至:${viewModel.phone}")
            Spacer(modifier = Modifier.height(16.dp))
            TextButton(onClick = { /*TODO*/ }) {
                Text(text = "未收到验证吗？")
            }
        }
    }
}