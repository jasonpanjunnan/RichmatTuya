package com.richmat.mytuya.ui.sign.compoments

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.richmat.mytuya.ui.sign.select_country.SelectCountryViewModel

@Composable
fun SelectCountryScreen(
    navController: NavController,
    viewModel: SelectCountryViewModel = hiltViewModel(),
) {
    val countries by viewModel.countries.collectAsState()
    Column(modifier = Modifier.fillMaxWidth()) {
        Row(modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically) {
            IconButton(onClick = { /*TODO*/ }) {
                Icon(imageVector = Icons.Default.ArrowBack,
                    contentDescription = null)
            }
            BasicTextField(value = "sss", onValueChange = { })
        }
        LazyColumn {
            items(countries) { country ->
                Text(text = country.first, modifier = Modifier.fillMaxWidth())
            }
        }
    }
}