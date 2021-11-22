package com.richmat.mytuya.ui.domain.use_case

import android.util.Log
import com.alibaba.fastjson.JSONObject
import com.richmat.mytuya.ui.domain.repository.UserRepository
import com.tuya.smart.android.base.bean.CountryRespBean

class GetCountries(
    private val repository: UserRepository,
) {
    suspend operator fun invoke(): List<Pair<String, String>> {
        try {
            val countryJson = repository.getCountries()
            val countries = JSONObject.parseArray(countryJson, CountryRespBean::class.java)
            Log.e(
                "TAG",
                "invoke: ${countries[0].a},${countries[0].c},${countries[0].n},${countries[0].p}",
            )
            return countries.map { countryRespBean ->
                Pair(countryRespBean.n, countryRespBean.a)
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }
        return emptyList()
    }
}