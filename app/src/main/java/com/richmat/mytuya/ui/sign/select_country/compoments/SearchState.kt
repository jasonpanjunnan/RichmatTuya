package com.richmat.mytuya.ui.sign.select_country.compoments

import androidx.compose.runtime.*
import androidx.compose.ui.text.input.TextFieldValue
import com.tuya.smart.android.base.bean.CountryRespBean

enum class SearchDisplay {
    Categories, Suggestions, Results, NoResults
}

@Composable
fun rememberCountrySearchState(
    query: TextFieldValue = TextFieldValue(""),
    focused: Boolean = false,
    searching: Boolean = false,
    searchResults: List<CountryRespBean> = emptyList(),
): CountrySearchState {
    return remember {
        CountrySearchState(
            query = query,
            focused = focused,
            searching = searching,
            searchResults = searchResults
        )
    }

}

@Stable
class CountrySearchState(
    query: TextFieldValue,
    focused: Boolean,
    searching: Boolean,
    searchResults: List<CountryRespBean>,
) {
    var query by mutableStateOf(query)
    var focused by mutableStateOf(focused)
    var searching by mutableStateOf(searching)
    var searchResults by mutableStateOf(searchResults)
    val searchDisplay: SearchDisplay
        get() = when {
            !focused && query.text.isEmpty() -> SearchDisplay.Categories
            focused && query.text.isEmpty() -> SearchDisplay.Suggestions
            searchResults.isEmpty() -> SearchDisplay.NoResults
            else -> SearchDisplay.Results
        }
}