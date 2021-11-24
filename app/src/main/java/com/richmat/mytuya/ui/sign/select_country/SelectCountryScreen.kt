package com.richmat.mytuya.ui.sign.compoments

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment.Companion.CenterHorizontally
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.richmat.mytuya.ui.sign.select_country.SelectCountryEvent
import com.richmat.mytuya.ui.sign.select_country.SelectCountryViewModel
import com.richmat.mytuya.ui.sign.select_country.compoments.CountrySearchState
import com.richmat.mytuya.ui.sign.select_country.compoments.SearchCountry
import com.richmat.mytuya.ui.sign.select_country.compoments.rememberCountrySearchState
import com.richmat.mytuya.util.getCountryName
import com.richmat.mytuya.util.getCountryPhoneticize

@Composable
fun SelectCountryScreen(
    navController: NavController,
    viewModel: SelectCountryViewModel = hiltViewModel(),
    state: CountrySearchState = rememberCountrySearchState(),
) {
    val countries by viewModel.countries.collectAsState()

    //记录列表的大部分信息，如：回滚第一行
    val listState = rememberLazyListState()
    val scope = rememberCoroutineScope()

    Scaffold(
        topBar = {
            TopAppBar(
                navigationIcon = {
                    TextButton(onClick = {
                        navController.navigateUp()
                    }) {
                        Text(text = "取消",
                            color = MaterialTheme.colors.onPrimary,
                            style = MaterialTheme.typography.body2)
                    }
                },
                actions = {},
                title = {
                    Text(text = "选择国家/地区",
                        style = MaterialTheme.typography.h5,
                        modifier = Modifier
                            .fillMaxWidth(0.6f)
                            .wrapContentWidth(CenterHorizontally))
                }
            )
        }
    ) {
        Column(modifier = Modifier.fillMaxWidth()) {
            SearchCountry(modifier = Modifier.fillMaxWidth(),
                search = { viewModel.onEvent(SelectCountryEvent.Search(it)) },
                itemClick = {
                    viewModel.onEvent(SelectCountryEvent.SelectedCountry(it))
                    navController.navigateUp()
                },
                state = state)
            Divider()
            if (!state.focused) {
                LazyColumn(
                    modifier = Modifier.fillMaxSize(),
                    contentPadding = PaddingValues(bottom = 10.dp, start = 16.dp, end = 16.dp),
                    state = listState
                ) {
                    val groups = countries.groupBy { country ->
                        country.getCountryPhoneticize()[0].lowercase()
                    }
                    groups.forEach { (initial, list) ->
                        item {
                            Text(text = initial.uppercase(),
                                modifier = Modifier.fillMaxWidth(),
                                style = MaterialTheme.typography.h6)
                        }
                        items(list) { country ->
                            TextButton(onClick = {
                                viewModel.onEvent(SelectCountryEvent.SelectedCountry(country))
                                navController.navigateUp()
                            }, modifier = Modifier.fillMaxWidth()) {
                                Text(text = country.getCountryName(),
                                    modifier = Modifier.fillMaxWidth(),
                                    color = MaterialTheme.colors.onPrimary)
                            }
                            Divider()
                        }
                    }
                }
            }
        }
    }
}