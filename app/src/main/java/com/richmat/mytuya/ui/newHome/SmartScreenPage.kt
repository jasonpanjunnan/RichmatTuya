package com.richmat.mytuya.ui.newHome

import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.material.Button
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import com.example.compose.jetsurvey.theme.JetsurveyTheme
import com.richmat.mytuya.ui.components.HomeMenu
import com.richmat.mytuya.util.supportWideScreen

@ExperimentalAnimationApi
@Composable
fun SmartScreenPage(navController: NavHostController) {
//        Text(text = "this is Smart page", style = MaterialTheme.typography.h4)
    JetsurveyTheme() {
        SmartScreen(navController)
    }
}

@ExperimentalAnimationApi
@Composable
fun SmartScreen(navController: NavHostController) {
    Column() {
        HomeMenu(navController = navController, homeID = 1000)
        AutomaticTab()
        Box(
            modifier = Modifier
                .supportWideScreen()
                .weight(1f),
            contentAlignment = Alignment.Center
        ) {
            Button(onClick = { /*TODO*/ }) {
                Text(text = "创建智能场景")
            }
        }
    }
}