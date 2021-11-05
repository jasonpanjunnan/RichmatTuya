package com.richmat.mytuya.ui.newHome

import androidx.compose.animation.animateColor
import androidx.compose.animation.core.*
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.Image
import androidx.compose.foundation.combinedClickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.GridCells
import androidx.compose.foundation.lazy.LazyVerticalGrid
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Done
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import coil.compose.rememberImagePainter
import com.example.compose.jetsurvey.theme.JetsurveyTheme
import com.example.compose.jetsurvey.theme.Red300
import com.richmat.mytuya.R
import com.tuya.smart.sdk.bean.DeviceBean

@ExperimentalComposeUiApi
@ExperimentalFoundationApi
@Composable
fun RemoveScreen(
    list: List<DeviceBean>,
    onLongClick: () -> Unit = {},
    onclick: (String) -> Unit = {},
    isSelect: (String) -> Boolean = { false },
    removeSetState: () -> Boolean = { false },
    removeDeviceSet: () -> Unit = { },
    ondismiss: () -> Unit,
) {
    var confirmRemove by rememberSaveable {
        mutableStateOf(false)
    }
    Dialog(
        onDismissRequest = ondismiss,
        properties = DialogProperties(usePlatformDefaultWidth = false)
    ) {
        Scaffold(
            topBar = {
                TopAppBar(
                    navigationIcon = {
                        IconButton(onClick = ondismiss) {
                            Icon(imageVector = Icons.Filled.Close, contentDescription = "close")
                        }
                    },
                    title = {
                        Text(
                            text = stringResource(id = R.string.device_manager),
                            modifier = Modifier.fillMaxWidth(),
                            textAlign = TextAlign.Center,
                            style = MaterialTheme.typography.h6
                        )
                    },
                    actions = {
                        val resetEnabled = !removeSetState()

                        IconButton(
                            onClick = { confirmRemove = true },
//                            onClick = removeDeviceSet,
                            enabled = resetEnabled
                        ) {
                            val alpha = if (resetEnabled) {
                                ContentAlpha.high
                            } else {
                                ContentAlpha.disabled
                            }
                            CompositionLocalProvider(LocalContentAlpha provides alpha) {
                                Text(
                                    text = stringResource(id = R.string.remove),
                                    style = MaterialTheme.typography.body2
                                )
                            }
                        }
                    }
                )
            }
        ) {
//            Box(
//                modifier = Modifier
//                    .fillMaxSize()
//                    .background(Gray100)
////                    .padding(horizontal = 24.dp, vertical = 16.dp)
//            ) {
            LazyVerticalGrid(
                modifier = Modifier
                    .fillMaxSize(),
                cells = GridCells.Fixed(2),
                contentPadding = PaddingValues(bottom = 55.dp),
            ) {
                items(list) { device ->
                    val infiniteTransition = rememberInfiniteTransition()
                    val degrees by infiniteTransition.animateFloat(
                        initialValue = 30f,
                        targetValue = -30f,
                        animationSpec = infiniteRepeatable(
                            animation = tween(800, easing = LinearEasing),
                            repeatMode = RepeatMode.Reverse
                        )
                    )
                    Card(backgroundColor = Color.White,
                        modifier = Modifier
                            .padding(10.dp)
                            .rotate(degrees = degrees)) {
                        var removeScreenVisible by rememberSaveable { mutableStateOf(false) }
                        Box() {
                            Column(modifier = Modifier
//                                    .background(Color.White)
                                .combinedClickable(onLongClick = onLongClick) {
                                    removeScreenVisible = !removeScreenVisible
                                    onclick(device.devId)
                                }
                            ) {
                                Image(
                                    painter = rememberImagePainter(data = device.iconUrl,
                                        builder = {
                                            crossfade(true)
                                            placeholder(drawableResId = R.drawable.gate)
                                        }),
                                    contentDescription = null,
                                    modifier = Modifier
                                        .padding(10.dp)
                                        .sizeIn(
                                            minHeight = 40.dp,
                                            minWidth = 40.dp,
                                        )
                                )
                                Text(
                                    text = device.name, style = MaterialTheme.typography.h6,
                                    fontSize = 17.sp,
                                    modifier = Modifier.padding(5.dp),
                                    maxLines = 1
                                )
                                Row(modifier = Modifier.height(IntrinsicSize.Min)) {
                                    Text(
                                        text = device.lat, style = MaterialTheme.typography.h4,
                                        fontSize = 13.sp,
                                        modifier = Modifier
                                            .padding(5.dp)
                                            .padding(start = 4.dp)
                                            .weight(1f)
                                            .wrapContentWidth(Alignment.Start),
                                        maxLines = 1
                                    )
                                }
                            }
                            SelectTopicButton(
                                modifier = Modifier
                                    .align(Alignment.TopEnd)
                                    .padding(10.dp),
                                selected = isSelect(device.devId)
                            )
                        }
                    }
                }
            }
//            }
        }
    }

    if (confirmRemove) {
        RemoveDialog(
            removeDeviceSet,
            dismiss = { confirmRemove = false },
            modifier = Modifier.wrapContentSize(Alignment.Center)
        )
    }
}

@Composable
fun RemoveDialog(
    removeDeviceSet: () -> Unit,
    modifier: Modifier = Modifier,
    dismiss: () -> Unit = {},
) {
    AlertDialog(
        modifier = modifier,
        onDismissRequest = dismiss,
        confirmButton = {
            TextButton(onClick = {
                removeDeviceSet()
                dismiss()
            }) {
                Text(text = stringResource(id = R.string.confirm), color = Red300)
            }
        },
        dismissButton = {
            TextButton(onClick = dismiss) {
                Text(text = stringResource(id = R.string.cancel), color = Red300)
            }
        },
        title = { Text(text = stringResource(id = R.string.confirm_remove)) },
        text = { Text(text = stringResource(id = R.string.confirm_remove_text)) })
}

@Composable
fun SelectTopicButton(
    modifier: Modifier = Modifier,
    selected: Boolean = false,
) {
    val icon = if (selected) Icons.Filled.Done else Icons.Filled.Add
    val iconColor = if (selected) MaterialTheme.colors.onPrimary else MaterialTheme.colors.primary
    val borderColor =
        if (selected) MaterialTheme.colors.primary else MaterialTheme.colors.onSurface.copy(alpha = 0.1f)
    val backgroundColor = if (selected) {
        MaterialTheme.colors.primary
    } else {
        MaterialTheme.colors.onPrimary
    }
    Surface(
        color = backgroundColor,
        shape = CircleShape,
        border = BorderStroke(1.dp, borderColor),
        modifier = modifier.size(30.dp, 30.dp)
    ) {
        Image(
            imageVector = icon,
            colorFilter = ColorFilter.tint(iconColor),
            modifier = Modifier.padding(8.dp),
            contentDescription = null // toggleable at higher level
        )
    }
}


@ExperimentalComposeUiApi
@ExperimentalFoundationApi
@Preview
@Composable
fun showScreen() {
    JetsurveyTheme {
        RemoveScreen(emptyList()) {

        }
    }
}