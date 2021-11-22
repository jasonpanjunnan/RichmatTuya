package com.richmat.mytuya.ui.newHome

import androidx.annotation.StringRes
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Home
import androidx.compose.material.icons.rounded.LightMode
import androidx.compose.material.icons.rounded.PersonPinCircle
import androidx.compose.ui.graphics.vector.ImageVector
import com.richmat.mytuya.R

//lei si mei ju
sealed class Page(val route: String) {
    object Home : Page("home_page")
    object Smart : Page("smart_page")
    object Myself : Page("myself_page")
    object SearchDevice : Page("searchDevice_page")
    object SignIn : Page("signIn_page")
    object SignUp : Page("signUp_page")
    object Login : Page("login_page")
    object VerificationCode : Page("verificationCode_page")
    object SetPassword : Page("setPassword_page")
    object Setting : Page("setting_page")
    object SelectDevice : Page("selectDevice_page")
    object SearchUiState : Page("searchUiState_page")
    object DevSetting : Page("devSetting_page")
}

sealed class DevicePage(val route: String) {
    object Gateway : DevicePage("gateway_devicePage")
    object DetailDevice : DevicePage("detailDevice_devicePage")
    object SearchChildDevice : DevicePage("searchDevice_devicePage")
    object BodySensor : DevicePage("bodySensor_devicePage")
    object BulbLight : DevicePage("bulbLight_devicePage")
}

sealed class Login(val route: String) {
    object LoginScreen : Login("login_screen")
}

sealed class TabItem(val page: Page, @StringRes val resourceId: Int, val iconImage: ImageVector) {
    object HomeTab : TabItem(Page.Home, R.string.home, Icons.Rounded.Home)
    object SmartTab : TabItem(Page.Smart, R.string.smart, Icons.Rounded.LightMode)
    object MyselfTab : TabItem(Page.Myself, R.string.myself, Icons.Rounded.PersonPinCircle)
}



