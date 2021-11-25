package com.richmat.mytuya.ui.sign.select_country.compoments

import android.content.res.Configuration
import android.util.Log
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.ArrowBack
import androidx.compose.material.icons.outlined.Search
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.onFocusChanged
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.google.accompanist.insets.statusBarsPadding
import com.richmat.mytuya.R
import com.richmat.mytuya.util.getCountryName
import com.tuya.smart.android.base.bean.CountryRespBean
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

@Composable
fun SearchCountry(
    modifier: Modifier = Modifier,
    search: (String) -> List<CountryRespBean>,
    itemClick: (CountryRespBean) -> Unit,
    state: CountrySearchState = rememberCountrySearchState(),
    categories: @Composable () -> Unit,
) {
    Column(modifier = modifier) {
        Spacer(modifier = Modifier
//            .fillMaxSize()
            .statusBarsPadding())
        SearchBar(
            query = state.query,
            onQueryChange = { state.query = it },
            searchFocused = state.focused,
            onSearchFocusChange = { state.focused = it },
            onClearQuery = { state.query = TextFieldValue("") },
            searching = state.searching
        )
        Log.e("TAG", "SearchCountry: ${state.query.text}")
        Divider()
        //TODO 会闪一下，太灵敏了，状态被重置了一次，应该是
        LaunchedEffect(key1 = state.query.text) {
            state.searching = true
//            state.searchResults = search(state.query.text)
            state.searchResults = withContext(Dispatchers.Default) {
                search(state.query.text)
            }
            Log.e("TAG", "SearchCountry: ${state.searchResults},${state.searchDisplay}")
            state.searching = false
        }
        when (state.searchDisplay) {
            SearchDisplay.Categories -> categories()
            SearchDisplay.Suggestions -> {}
            SearchDisplay.Results -> {
                LazyColumn(modifier = Modifier.fillMaxSize()) {
                    items(state.searchResults) { country ->
                        TextButton(onClick = { itemClick(country) },
                            modifier = Modifier.fillMaxWidth()) {
                            Text(text = country.getCountryName(),
                                modifier = Modifier.fillMaxWidth(),
                                color = MaterialTheme.colors.onPrimary)
                        }
                    }
                }
            }
            SearchDisplay.NoResults -> {}
        }
    }
}

@Composable
private fun SearchBar(
    query: TextFieldValue,
    onQueryChange: (TextFieldValue) -> Unit,
    searchFocused: Boolean,
    onSearchFocusChange: (Boolean) -> Unit,
    onClearQuery: () -> Unit,
    searching: Boolean,
    modifier: Modifier = Modifier,
) {
    Box(
        Modifier
            .fillMaxWidth()
            .height(56.dp)
            .padding(horizontal = 24.dp, vertical = 8.dp)
    ) {
        if (query.text.isEmpty()) {
            SearchHint()
        }
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier
                .fillMaxSize()
                .height(40.dp)
//                .align(CenterVertically)
                .background(Color.LightGray.copy(0.2f), RoundedCornerShape(18.dp))

//                .wrapContentHeight()
        ) {
            if (searchFocused) {
                IconButton(onClick = onClearQuery) {
                    Icon(
                        imageVector = Icons.Outlined.ArrowBack,
                        contentDescription = null
                    )
                }
            }
            BasicTextField(
                value = query,
                onValueChange = onQueryChange,
                modifier = Modifier
                    .weight(1f)
                    .onFocusChanged {
                        onSearchFocusChange(it.isFocused)
                    }
                    .padding(start = 10.dp),
//                decorationBox = { innerTextField ->
//                    Box(
//                        modifier = Modifier.weight(1f)
//                            .background(Color.LightGray.copy(0.2f), RoundedCornerShape(8.dp)),
//                        contentAlignment = Alignment.CenterStart
//                    ) {
//                        innerTextField()
//                    }
//                }
            )
            if (searching) {
                CircularProgressIndicator(
                    color = Color.Blue,
                    modifier = Modifier
                        .padding(horizontal = 6.dp)
                        .size(36.dp)
                )
            } else {
                Spacer(Modifier.width(36.dp)) // balance arrow icon
            }
        }
    }
}

@Composable
private fun SearchHint() {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = Modifier
            .fillMaxSize()
            .wrapContentSize()
    ) {
        Icon(
            imageVector = Icons.Outlined.Search,
//            tint = JetsnackTheme.colors.textHelp,
            contentDescription = null
        )
        Spacer(Modifier.width(8.dp))
        Text(
            text = stringResource(id = R.string.search),
//            color = JetsnackTheme.colors.textHelp
        )
    }
}

@Preview("default")
@Preview("dark theme", uiMode = Configuration.UI_MODE_NIGHT_YES)
@Preview("large font", fontScale = 2f)
@Composable
fun ShowSearch() {
    SearchBar(
        query = TextFieldValue(""),
        onQueryChange = { },
        searchFocused = false,
        onSearchFocusChange = { },
        onClearQuery = { },
        searching = false
    )
}