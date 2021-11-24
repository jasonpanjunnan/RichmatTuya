package com.richmat.mytuya.ui.domain.use_case

import com.alibaba.fastjson.JSONObject
import com.richmat.mytuya.ui.domain.repository.UserRepository
import com.tuya.smart.android.base.bean.CountryRespBean

class GetCountries(
    private val repository: UserRepository,
) {
    suspend operator fun invoke(): List<CountryRespBean> {
        try {
            val countryJson = repository.getCountries()
            //            return countries.map { countryRespBean ->
//                Pair(countryRespBean.n, countryRespBean.c)
//            }
            return JSONObject.parseArray(countryJson, CountryRespBean::class.java)
        } catch (e: Exception) {
            e.printStackTrace()
        }
        return emptyList()
    }
}