package com.richmat.mytuya.ui.domain.model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class User(
    val name: String,
    val password: String,
    val countryCode: String,
    val phone: String,
    @PrimaryKey val id: Int? = null,
) {
    companion object {
    }
}

class InvalidUserException(errorMessage: String) : Exception(errorMessage)
