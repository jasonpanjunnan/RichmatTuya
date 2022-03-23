package com.richmat.mytuya.util

import com.alibaba.fastjson.JSON
import com.alibaba.fastjson.JSONObject
import com.richmat.mytuya.util.data.DevResultMassage
import com.tuya.smart.home.sdk.TuyaHomeSdk
import com.tuya.smart.sdk.bean.DeviceBean

fun getDsp(bean: DeviceBean): String {
    val dps = TuyaHomeSdk.getDataInstance().getDps(bean.getDevId())
    var details: String = ""
    dps?.forEach {
        details += "key: ${it.key} , value :${it.value}"
    }
    return details
}

fun jsonToDevResultMassage(jsonString : String): DevResultMassage =
    JSON.toJavaObject(JSONObject.parseObject(jsonString), DevResultMassage::class.java)

