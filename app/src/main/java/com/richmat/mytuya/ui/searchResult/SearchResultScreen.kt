package com.richmat.mytuya.ui.searchResult

import androidx.compose.animation.core.*
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Segment
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Alignment.Companion.CenterEnd
import androidx.compose.ui.Alignment.Companion.CenterHorizontally
import androidx.compose.ui.Alignment.Companion.CenterStart
import androidx.compose.ui.Alignment.Companion.End
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.compose.jetsurvey.theme.JetsurveyTheme
import com.example.compose.jetsurvey.theme.Red300
import com.example.compose.jetsurvey.theme.Red800
import com.richmat.mytuya.R
import com.richmat.mytuya.ui.components.DevPicAndName


@Composable
fun SearchResultScreen(
    complete: (String) -> Unit,
    viewModel: SearchResultViewModel,
) {
    val state by viewModel.uiState.collectAsState()
    ResultScreen(
        modifier = Modifier.fillMaxSize(),
        onClick = {
            complete(state.route)
//            complete("${state.route}/${"yingyingyingyingyingying"}")
//            complete(state.devResultMassage.getNavigationArgumentRoute())
        },
        name = state.name,
        onValueChange = { viewModel.changeName(it) },
        rename = { viewModel.rename() }
    )
}

@Composable
fun ResultScreen(
    modifier: Modifier = Modifier,
    onClick: () -> Unit,
    name: String,
    onValueChange: (String) -> Unit,
    rename: () -> Unit,
) {
    var showDialog by remember {
        mutableStateOf(false)
    }
    Box(modifier = modifier) {
        Column(modifier = Modifier.fillMaxWidth()) {
            TextButton(onClick = onClick, modifier = Modifier
                .align(End)
                .padding(6.dp)
            ) {
                Text(text = stringResource(id = R.string.complete), color = Red300,
                    fontSize = 17.sp)
            }

            Text(text = stringResource(
                id = R.string.add_device_finish),
                fontSize = 21.sp,
                modifier = Modifier
                    .padding(start = 15.dp, top = 20.dp, bottom = 15.dp))

            DevPicAndName(name,
                showDetail = { showDialog = true },
                onWidgetClick = onClick,
                painter = painterResource(id = R.drawable.add_success),
                subtitle = stringResource(id = R.string.add_success),
                modifier = modifier
                    .padding(vertical = 10.dp, horizontal = 10.dp))

            Box(Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {

                val infiniteTransition = rememberInfiniteTransition()
                val degrees by infiniteTransition.animateFloat(
                    initialValue = 30f,
                    targetValue = -30f,
                    animationSpec = infiniteRepeatable(
                        animation = tween(800, easing = LinearEasing),
                        repeatMode = RepeatMode.Reverse
                    )
                )

                Box(modifier = Modifier
                    .rotate(degrees)
                    .size(40.dp)
                    .background(Red800)
                    .wrapContentWidth()
                ) {

                }
                Box(modifier = Modifier
                    .rotate(150f)
                    .size(40.dp)
                    .background(Color.Blue)
                    .align(CenterStart)
                ) {

                }
                Box(modifier = Modifier.wrapContentSize()
                    .rotate(-150f)
                    .size(40.dp)
                    .background(Color.Blue)
                    .align(CenterEnd)

                ) {

                }
            }
        }
    }

    if (showDialog) {
        RenameDialog(value = name,
            onValueChange = onValueChange, onDismiss = { showDialog = false }, rename = rename)
    }
}

@Composable
fun RenameDialog(
    value: String,
    onValueChange: (String) -> Unit,
    onDismiss: () -> Unit,
    rename: () -> Unit,
) {
    AlertDialog(
        modifier = Modifier.wrapContentWidth(CenterHorizontally),
        title = {
            Text(modifier = Modifier.fillMaxWidth(),
                text = stringResource(id = R.string.rename),
                style = MaterialTheme.typography.h5)
        },
        text = {
            TextField(value = value,
                onValueChange = {
                    onValueChange(it)
                },
                colors = TextFieldDefaults.textFieldColors(backgroundColor = Color.Transparent,
                    textColor = Color.Black))
        },
        onDismissRequest = onDismiss,
        dismissButton = {
            TextButton(onClick = onDismiss) {
                Text(text = stringResource(id = R.string.cancel), color = Red800.copy(alpha = 0.8f))
            }
        },
        confirmButton = {
            TextButton(onClick = {
                rename()
                onDismiss()
            }) {
                Text(text = stringResource(id = R.string.confirm),
                    color = Red800)
            }
        }
    )
}

@Preview(name = "1111")
@Composable
fun ShowSearchResult() {
//    JetsurveyTheme {
    ResultScreen(onClick = {}, name = "zigbee", onValueChange = {}) {}
//    }
}

@Preview
@Composable
fun ShowDia() {
    JetsurveyTheme {
        RenameDialog(value = "", onValueChange = {}, onDismiss = {}) {}
    }
}
