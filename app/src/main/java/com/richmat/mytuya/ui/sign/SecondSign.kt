package com.richmat.mytuya.ui.sign

import android.widget.Toast
import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.foundation.layout.*
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.NavHostController
import com.richmat.mytuya.ui.components.Email
import com.richmat.mytuya.ui.components.Password
import com.example.compose.jetsurvey.signinsignup.PasswordState
import com.richmat.mytuya.ui.components.SignInSignUpScreen
import com.example.compose.jetsurvey.theme.JetsurveyTheme
import com.google.accompanist.navigation.animation.rememberAnimatedNavController
import com.richmat.mytuya.MyApplication
import com.richmat.mytuya.R
import com.richmat.mytuya.ui.components.SignInSignUpTopAppBar
import com.richmat.mytuya.ui.newHome.TabItem
import com.richmat.mytuya.ui.sign.textFieldState.PhoneState
import com.richmat.mytuya.util.supportWideScreen
import com.tuya.smart.android.user.api.ILoginCallback
import com.tuya.smart.android.user.bean.User
import com.tuya.smart.home.sdk.TuyaHomeSdk
import kotlinx.coroutines.launch

@Composable
fun SecondSignInScreen(navController: NavHostController) {
    val snackbarHostState = remember { SnackbarHostState() }
    val scope = rememberCoroutineScope()
    val snackbarErrorText = stringResource(id = R.string.feature_not_available)
    val snackbarActionLabel = stringResource(id = R.string.dismiss)

    Scaffold(
        topBar = {
            SignInSignUpTopAppBar(
                topAppBarText = stringResource(id = R.string.sign_in),
                onBackPressed = { navController.navigateUp() }
            )
        },
        content = {
            SignInSignUpScreen(
                modifier = Modifier.supportWideScreen(),
                onSignedInAsGuest = { }
            ) {
                Column(modifier = Modifier.fillMaxWidth()) {
                    SignInContent(
                        onSignInSubmitted = { phone, password ->
                            //账号密码登陆
                            TuyaHomeSdk.getUserInstance()
                                .loginWithPhonePassword(
                                    "86",
                                    phone,
                                    password,
                                    object : ILoginCallback {
                                        override fun onSuccess(user: User) {
//                                            Toast.makeText(
//                                                MyApplication.context,
//                                                "Login success：" + TuyaHomeSdk.getUserInstance().user!!.username,
//                                                Toast.LENGTH_SHORT
//                                            ).show()

                                            navController.navigate((TabItem.HomeTab.page.route)) {
                                                //清空顶上的回收栈
                                                navController.popBackStack()
                                                popUpTo(navController.graph.findStartDestination().id) {
                                                    //此项可控制是否退出首项 StartDestination
                                                    inclusive = true
                                                    saveState = true
                                                }
                                                launchSingleTop = true
                                                restoreState = true
                                            }
                                        }

                                        override fun onError(code: String, error: String) {
//                                            Toast.makeText(
//                                                MyApplication.context,
//                                                "code: " + code + "error: $error, /n 账号或密码错误，请重试",
//                                                Toast.LENGTH_SHORT
//                                            ).show()
                                        }
                                    })

                        }
                    )
                    Spacer(modifier = Modifier.height(16.dp))
                    TextButton(
                        onClick = {
                            scope.launch {
                                snackbarHostState.showSnackbar(
                                    message = snackbarErrorText,
                                    actionLabel = snackbarActionLabel
                                )
                            }
                        },
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Text(
                            text = stringResource(id = R.string.forgot_password),
                            color = Color.Blue
                        )
                    }
                }
            }
        }
    )

//    Box(modifier = Modifier.fillMaxSize()) {
//        ErrorSnackbar(
//            snackbarHostState = snackbarHostState,
//            onDismiss = { snackbarHostState.currentSnackbarData?.dismiss() },
//            modifier = Modifier.align(Alignment.BottomCenter)
//        )
//    }
}

@Composable
fun SignInContent(
    onSignInSubmitted: (email: String, password: String) -> Unit,
) {
    Column(modifier = Modifier.fillMaxWidth()) {
        val focusRequester = remember { FocusRequester() }
        val emailState = remember { PhoneState() }
        Email(emailState, onImeAction = { focusRequester.requestFocus() })

        Spacer(modifier = Modifier.height(16.dp))

        val passwordState = remember { PasswordState() }
        Password(
            label = stringResource(id = R.string.password),
            passwordState = passwordState,
            modifier = Modifier.focusRequester(focusRequester),
            onImeAction = { onSignInSubmitted(emailState.text, passwordState.text) }
        )
        Spacer(modifier = Modifier.height(16.dp))
        Button(
            onClick = { onSignInSubmitted(emailState.text, passwordState.text) },
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 16.dp),
            enabled = emailState.isValid && passwordState.isValid
        ) {
            Text(
                text = stringResource(id = R.string.sign_in)
            )
        }
    }
}

@ExperimentalAnimationApi
@Preview
@Composable
fun showSignUpScreen() {
    JetsurveyTheme {
        SecondSignInScreen(navController = rememberAnimatedNavController())
    }
}