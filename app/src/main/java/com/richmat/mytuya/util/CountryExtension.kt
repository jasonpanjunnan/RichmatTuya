package com.richmat.mytuya.util

import com.tuya.smart.android.base.bean.CountryRespBean

fun CountryRespBean.getCountryCode(): String = this.c

fun CountryRespBean.getCountryName(): String = this.n

fun CountryRespBean.getCountryPhoneticize(): String = this.p

fun CountryRespBean.default(): CountryRespBean = CountryRespBean().apply {
    a = ""
    c = "86"
    p = "zhongguo"
    n = "中国"
}
