package com.richmat.mytuya.ui.Home.personal_setting

import java.io.File

sealed class PersonalSettingEvent {
    data class UpdateNickName(val nickName: String) :
        PersonalSettingEvent()

    data class UploadUserAvatar(val file: File, val selectPicture: () -> Unit) :
        PersonalSettingEvent()
}
