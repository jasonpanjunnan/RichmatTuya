package com.richmat.mytuya

import android.annotation.SuppressLint
import android.app.Application
import android.content.Context
import com.tuya.smart.home.sdk.TuyaHomeSdk
import dagger.hilt.android.HiltAndroidApp

@HiltAndroidApp
class MyApplication : Application() {
    companion object {
        @SuppressLint("StaticFieldLeak")
        lateinit var context: Context

    }

    override fun onCreate() {
        super.onCreate()
        context = applicationContext
        TuyaHomeSdk.init(this)
        TuyaHomeSdk.setDebugMode(true)
    }

    override fun onTerminate() {
        super.onTerminate()
        TuyaHomeSdk.onDestroy()
    }

}