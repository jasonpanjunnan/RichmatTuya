package com.richmat.mytuya.ui.sign.select_country

import com.tuya.smart.android.base.bean.CountryRespBean

sealed class SelectCountryEvent {
    data class SelectedCountry(val country: CountryRespBean) : SelectCountryEvent()
    data class Search(val text: String) : SelectCountryEvent()
}
