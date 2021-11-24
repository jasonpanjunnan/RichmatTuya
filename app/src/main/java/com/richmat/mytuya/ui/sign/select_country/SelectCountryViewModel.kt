package com.richmat.mytuya.ui.sign.select_country

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.richmat.mytuya.ui.domain.use_case.UserUseCase
import com.richmat.mytuya.util.getCountryName
import com.richmat.mytuya.util.getCountryPhoneticize
import com.tuya.smart.android.base.bean.CountryRespBean
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject

@HiltViewModel
class SelectCountryViewModel @Inject constructor(
    private val userUseCase: UserUseCase,
) : ViewModel() {
    private val _countries = MutableStateFlow<List<CountryRespBean>>(emptyList())
    val countries: StateFlow<List<CountryRespBean>> = _countries

    init {
        viewModelScope.launch {
            _countries.value = userUseCase.getCountries().sortedBy { country ->
                country.getCountryPhoneticize().lowercase()
            }
        }
    }

    fun onEvent(event: SelectCountryEvent): List<CountryRespBean> {
        when (event) {
            is SelectCountryEvent.SelectedCountry -> {
                viewModelScope.launch {
                    userUseCase.selectedCurrentCountry(event.country)
                }
            }
            is SelectCountryEvent.Search -> {
                var list: List<CountryRespBean> = emptyList()
                viewModelScope.launch {
                    list = withContext(Dispatchers.Default) {
                        delay(200L) // simulate an I/O delay
                        countries.value.filter {
                            it.getCountryName().contains(event.text, ignoreCase = true)
                        }
                    }
                }
                return list
            }
        }
        return emptyList()
    }

}