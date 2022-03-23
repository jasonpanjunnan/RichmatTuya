package com.richmat.mytuya.ui.Home.personal_setting

import androidx.compose.foundation.layout.*
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment.Companion.CenterHorizontally
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController

@Composable
fun PersonalSettingScreen(
    navController: NavController,
    viewModel: PersonalSettingViewModel = hiltViewModel(),
) {
    Scaffold(
        topBar = {
            TopAppBar(
                navigationIcon = {
                    IconButton(onClick = { navController.navigateUp() }) {
                        Icon(imageVector = Icons.Default.ArrowBack, contentDescription = null)
                    }
                },
                title = {
                    Text(text = "个人资料",
                        style = MaterialTheme.typography.h6,
                        modifier = Modifier
                            .fillMaxWidth(0.8f)
                            .wrapContentWidth(CenterHorizontally))
                },
                actions = {},
            )
        },
    ) {
        Column(modifier = Modifier.fillMaxWidth().padding(top = 16.dp)) {

        }
    }
}