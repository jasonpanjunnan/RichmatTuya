package com.richmat.mytuya.ui.domain.use_case

import com.richmat.mytuya.ui.domain.repository.UserRepository
import com.tuya.smart.android.base.bean.CountryRespBean
import kotlinx.coroutines.flow.Flow

class ObserveCountry(
    private val repository: UserRepository,
) {
    operator fun invoke(): Flow<CountryRespBean> {
        return repository.observeCountry()
    }
}