package com.richmat.mytuya

//import com.example.compose.jetsurvey.signinsignup.WelcomeScreen
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.KeyEvent
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.annotation.RequiresApi
import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.ui.ExperimentalComposeUiApi
import com.example.compose.jetsurvey.theme.JetsurveyTheme
import com.richmat.mytuya.ui.newHome.Page
import com.tuya.smart.home.sdk.TuyaHomeSdk
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.InternalCoroutinesApi

//可以通过homeScreen往里传入
var mNeedBackGesture: Boolean = false

@AndroidEntryPoint
class MainActivity : ComponentActivity() {

    @ExperimentalMaterialApi
    @ExperimentalComposeUiApi
    @RequiresApi(Build.VERSION_CODES.O)
    @InternalCoroutinesApi
    @ExperimentalFoundationApi
    @ExperimentalAnimationApi
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        var startPage: Page = Page.SignIn
        if (TuyaHomeSdk.getUserInstance().isLogin) {
//            startActivity()
            startPage = Page.Home
//            finish()
        }

        setContent {
            JetsurveyTheme {
                com.richmat.mytuya.ui.newHome.HomeScreen(startPage)
//                HomeScreen()
            }
//            WelcomeScreen({
//// 注册手机密码账户
//// 注册手机密码账户
//                TuyaHomeSdk.getUserInstance().registerAccountWithPhone(
//                    "86",
//                    "18263892309",
//                    "Richmat123",
//                    "281680",
//                    object : IRegisterCallback {
//                        override fun onSuccess(user: User) {
//                            Toast.makeText(this@MainActivity, "注册成功", Toast.LENGTH_SHORT).show()
//                        }
//
//                        override fun onError(code: String, error: String) {
//                            Toast.makeText(
//                                this@MainActivity,
//                                "code: " + code + "error:" + error,
//                                Toast.LENGTH_SHORT
//                            ).show()
//                        }
//                    })
//            }) {
            //账号密码登陆
//            TuyaHomeSdk.getUserInstance()
//                .loginWithPhonePassword(
//                    "86",
//                    "18263892309",
//                    "Richmat123",
//                    object : ILoginCallback {
//                        override fun onSuccess(user: User) {
//                            Toast.makeText(
//                                this@MainActivity,
//                                "Login success：" + TuyaHomeSdk.getUserInstance().user!!.username,
//                                Toast.LENGTH_SHORT
//                            ).show()
//                        }
//
//                        override fun onError(code: String, error: String) {
//                            Toast.makeText(
//                                this@MainActivity,
//                                "code: " + code + "error:" + error,
//                                Toast.LENGTH_SHORT
//                            ).show()
//                        }
//                    })
//
//                //游客账号登陆
////                    TuyaHomeSdk.getUserInstance().touristRegisterAndLogin("86", "MyTuya",object :
////                        IRegisterCallback {
////                        override fun onSuccess(user: User?) {
////                            Toast.makeText(this@MainActivity,"onSuccess:",Toast.LENGTH_SHORT).show()
////                            Log.e("TAG", "onSuccess: ", )
////                            startActivity()
////                        }
////
////                        override fun onError(code: String?, error: String?) {
////                            Toast.makeText(this@MainActivity,"onError: $code, $error",Toast.LENGTH_SHORT).show()
////                            Log.e("TAG", "onError: $code, $error", )
////                        }
////                    })
//            }
        }
    }

    override fun onResume() {
        super.onResume()
        // 获取手机验证码
        // 获取手机验证码
//        TuyaHomeSdk.getUserInstance().sendVerifyCodeWithUserName(
//            "18263892309",
//            "",
//            "86",
//            1,
//            object : IResultCallback {
//                override fun onError(code: String?, error: String?) {
//                    Toast.makeText(
//                        this@MainActivity,
//                        "code: " + code + "error:" + error,
//                        Toast.LENGTH_SHORT
//                    ).show()
//                }
//
//                override fun onSuccess() {
//                    Toast.makeText(this@MainActivity, "获取验证码成功", Toast.LENGTH_SHORT).show()
//                }
//
//            })
    }

    override fun dispatchKeyEvent(event: KeyEvent?): Boolean {
        Log.e("TAG", "dispatchKeyEvent: ${event?.action}， ${event?.keyCode} ")
        if (event?.keyCode == KeyEvent.KEYCODE_BACK && mNeedBackGesture) {
            Log.e("TAG", "dispatchKeyEvent: 成功拦截，你想干什么")
            return true
        }
        return super.dispatchKeyEvent(event)
    }
}
