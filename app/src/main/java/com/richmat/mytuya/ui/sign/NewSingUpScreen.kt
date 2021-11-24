package com.richmat.mytuya.ui.sign

import androidx.annotation.StringRes
import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.foundation.layout.*
import androidx.compose.material.Button
import androidx.compose.material.Scaffold
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import com.example.compose.jetsurvey.theme.JetsurveyTheme
import com.richmat.mytuya.R
import com.richmat.mytuya.ui.components.Email
import com.richmat.mytuya.ui.components.SignInSignUpScreen
import com.richmat.mytuya.ui.components.SignInSignUpTopAppBar
import com.richmat.mytuya.ui.newHome.Page
import com.richmat.mytuya.ui.sign.textFieldState.PhoneState
import com.richmat.mytuya.util.supportWideScreen

@Composable
fun NewSignUpScreen(navController: NavHostController) {
    JetsurveyTheme {
//        val snackbarHostState = remember { SnackbarHostState() }
//        val scope = rememberCoroutineScope()
//        val snackbarErrorText = stringResource(id = R.string.feature_not_available)
//        val snackbarActionLabel = stringResource(id = R.string.dismiss)

        Scaffold(
            topBar = {
                SignInSignUpTopAppBar(
                    topAppBarText = stringResource(id = R.string.sign_up),
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
                                navController.navigate((Page.VerificationCode.route)) {
                                }
                            }, R.string.sign_up
                        )
                    }
                }
            }
        )
    }
}

@Composable
fun NewSignInContent(
    onSignInSubmitted: (email: String, password: String) -> Unit, @StringRes id: Int
) {
    Column(modifier = Modifier.fillMaxWidth()) {
        val focusRequester = remember { FocusRequester() }
        val emailState = remember { PhoneState() }
        Email(emailState, onImeAction = { focusRequester.requestFocus() })

        Spacer(modifier = Modifier.height(16.dp))
        Button(
            onClick = { onSignInSubmitted(emailState.text, "passwordState.text") },
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 16.dp),
            enabled = emailState.isValid
        ) {
            Text(
                text = stringResource(id = id)
            )
        }
    }
}

@ExperimentalAnimationApi
@Preview
@Composable
fun showScreen() {
    NewSignUpScreen(rememberNavController())
}