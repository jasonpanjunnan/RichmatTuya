package com.richmat.mytuya.ui.domain.use_case

import com.richmat.mytuya.ui.domain.repository.UserRepository

class TouristRegisterAndLogin(
    private val repository: UserRepository,
) {
    suspend operator fun invoke(countryCode: String): Boolean {
        return try {
            repository.touristRegisterAndLogin(countryCode)
        } catch (e: Exception) {
            e.printStackTrace()
            false
        }
    }
}