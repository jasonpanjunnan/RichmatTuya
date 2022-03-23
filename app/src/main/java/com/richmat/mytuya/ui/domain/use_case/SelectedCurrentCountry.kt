package com.richmat.mytuya.ui.domain.use_case

import com.richmat.mytuya.ui.domain.repository.UserRepository
import com.tuya.smart.android.base.bean.CountryRespBean

class SelectedCurrentCountry(
    private val repository: UserRepository,
) {
    suspend operator fun invoke(country: CountryRespBean) {
        repository.selectedCurrentCountry(country)
    }
}