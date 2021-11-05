package com.richmat.mytuya.ui.newHome

import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.navigation.NavHostController
import com.google.accompanist.navigation.animation.rememberAnimatedNavController

@ExperimentalFoundationApi
@Composable
fun PersonalScreenPage(navController: NavHostController) {
    Box(
        modifier = Modifier
            .fillMaxSize()
//            , contentAlignment = Alignment.Start
    ) {
        PersonalScreen(navController)
    }
}

@ExperimentalAnimationApi
@ExperimentalFoundationApi
@Preview
@Composable
fun showHome() {
    PersonalScreenPage(rememberAnimatedNavController())
}