package com.richmat.mytuya.ui.sign.register

import androidx.compose.foundation.layout.*
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Menu
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.richmat.mytuya.ui.Home.Login
import com.richmat.mytuya.util.getCountryCode
import com.richmat.mytuya.util.getCountryName

@Composable
fun RegisterScreen(
    navController: NavController,
    viewModel: RegisterViewModel = hiltViewModel(),
) {
    val phone = viewModel.phone.value
    val country by viewModel.currentCountry.collectAsState()

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
            Text(text = "注册", style = MaterialTheme.typography.h4)
            Spacer(modifier = Modifier.height(16.dp))
            Button(onClick = {
                navController.navigate(Login.SelectCountryScreen.route)
            },
                modifier = Modifier.fillMaxWidth(),
                colors = ButtonDefaults.buttonColors(backgroundColor = Color.White)) {
                Text(text = country.getCountryName(),
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
                    viewModel.onEvent(RegisterEvent.EnterPhone(it))
                }, label = {
                    Text(text = "手机号")
                })
            Spacer(modifier = Modifier.height(32.dp))
            Button(onClick = {
                viewModel.onEvent(RegisterEvent.SendVerifyCodeWithUserName(
                    "",
                    phone,
                    country.getCountryCode(),
                    1
                ) {
                    navController.navigate(
                        Login.VerifyRegisterCodeScreen.route +
                                "?countryCode=${country.getCountryCode()}&phone=${phone}"
                    )
                })
            },
                modifier = Modifier.fillMaxWidth(),
                enabled = phone.isNotBlank()) {
                Text(text = "获取验证码", style = MaterialTheme.typography.body1)
            }
        }
    }
}