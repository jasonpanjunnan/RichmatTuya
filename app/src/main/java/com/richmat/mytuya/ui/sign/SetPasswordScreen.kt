package com.richmat.mytuya.ui.sign

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import com.richmat.mytuya.ui.components.SignInSignUpScreen
import com.richmat.mytuya.ui.components.SignInSignUpTopAppBar
import com.richmat.mytuya.ui.theme.JetsurveyTheme
import com.richmat.mytuya.R
import com.richmat.mytuya.ui.Home.TabItem
import com.richmat.mytuya.util.supportWideScreen

@Composable
fun SetPasswordScreen(navController: NavHostController) {
    JetsurveyTheme {
        Scaffold(
            topBar = {
                SignInSignUpTopAppBar(
                    topAppBarText = stringResource(id = R.string.set_password),
                    onBackPressed = { navController.navigateUp() }
                )
            },
            content = {
                SignInSignUpScreen(
                    modifier = Modifier.supportWideScreen(),
                    onSignedInAsGuest = { }
                ) {
                    Column(modifier = Modifier.fillMaxWidth()) {
                        NewSignInContent(
                            onSignInSubmitted = { email, password ->
                                navController.navigate((TabItem.HomeTab.page.route)) {
                                    popUpTo(navController.graph.findStartDestination().id) {
                                        //此项可控制是否退出首项 StartDestination
                                        inclusive = true
                                        saveState = true
                                    }
                                    launchSingleTop = true
                                    restoreState = true
                                }
                            }, R.string.confirm
                        )
                    }
                }
            }
        )
    }
}

@Preview
@Composable
fun ShowSetPasswordScreen() {
    SetPasswordScreen(rememberNavController())
}