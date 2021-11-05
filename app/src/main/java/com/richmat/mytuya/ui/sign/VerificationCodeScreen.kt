package com.richmat.mytuya.ui.sign

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.navigation.NavHostController
import com.richmat.mytuya.ui.components.SignInSignUpScreen
import com.richmat.mytuya.ui.components.SignInSignUpTopAppBar
import com.example.compose.jetsurvey.theme.JetsurveyTheme
import com.richmat.mytuya.R
import com.richmat.mytuya.ui.newHome.Page
import com.richmat.mytuya.util.supportWideScreen

@Composable
fun VerificationCodeScreen(navController: NavHostController) {
    JetsurveyTheme {
        Scaffold(
            topBar = {
                SignInSignUpTopAppBar(
                    topAppBarText = stringResource(id = R.string.verification_code),
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
                                navController.navigate((Page.SetPassword.route)) {
                                }
                            },R.string.confirm
                        )
                    }
                }
            }
        )
    }
}