package com.richmat.mytuya.ui.sign.select_country

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.richmat.mytuya.ui.domain.use_case.UserUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SelectCountryViewModel @Inject constructor(
    private val userUseCase: UserUseCase,
) : ViewModel() {
    private val _countries = MutableStateFlow<List<Pair<String, String>>>(emptyList())
    val countries: StateFlow<List<Pair<String, String>>> = _countries

    init {
        viewModelScope.launch {
            _countries.value = userUseCase.getCountries()
        }
    }
}