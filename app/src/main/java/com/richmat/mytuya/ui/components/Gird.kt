package com.richmat.mytuya.ui.components

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.combinedClickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.GridCells
import androidx.compose.foundation.lazy.LazyVerticalGrid
import androidx.compose.foundation.lazy.items
import androidx.compose.material.Card
import androidx.compose.material.Divider
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import coil.compose.rememberImagePainter
import com.richmat.mytuya.R
import com.richmat.mytuya.ui.Home.DevicePage
import com.tuya.smart.sdk.bean.DeviceBean

@ExperimentalFoundationApi
@Composable
fun NewGrid(
    list: List<DeviceBean>,
    navController: NavHostController,
    onLongClick: () -> Unit = {},
    onClick: ((String, String) -> Unit)? = null,
) {
    LazyVerticalGrid(
        cells = GridCells.Adaptive(150.dp),
        contentPadding = PaddingValues(bottom = 55.dp),
    ) {
        items(list) { device ->
            Card(backgroundColor = Color.White, modifier = Modifier.padding(10.dp)) {
                NewCardDevice(device,
                    navController,
                    onLongClick = onLongClick,
                    onClick = onClick,
                    painter = rememberImagePainter(data = device.iconUrl,
                        builder = {
                            crossfade(true)
                            placeholder(drawableResId = R.drawable.gate)
                        }
                    ))
            }
        }

//        itemsIndexed(list) { de, d ->
//
//        }
    }
}

@ExperimentalFoundationApi
@Composable
fun NewCardDevice(
    device: DeviceBean,
    navController: NavHostController,
    onLongClick: () -> Unit = {},
    onClick: ((deviceId: String, argument: String) -> Unit)? = null,
    painter: Painter,
) {
    Column(modifier = Modifier
        .combinedClickable(onLongClick = onLongClick,
            onClick = {
                onClick?.invoke(device.devId, "argument1")
            }
        )
    ) {
//        //TODO 传入设备信息，展示设备详情页面.10.29注，从网络获取的图片，必须要自己设定好大小
        Image(
//            Icons.Rounded.Layers,
            painter = painter,
            contentDescription = null,
            modifier = Modifier
                .padding(10.dp)
                .sizeIn(minHeight = 40.dp, minWidth = 40.dp, maxWidth = 50.dp, maxHeight = 50.dp)
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
            if (!device.isOnline) {
                Divider(
                    modifier = Modifier
                        .align(Alignment.CenterVertically)
                        .fillMaxHeight(0.5f)
                        .width(1.dp)
                        .background(Color.Black)
                )
                Text(
                    text = "已离线", style = MaterialTheme.typography.h5,
                    fontSize = 13.sp,
                    modifier = Modifier
                        .padding(5.dp)
                        .padding(start = 4.dp)
                        .weight(1f)
                        .wrapContentWidth(Alignment.End)
                )
            }
        }
    }
}

fun getRouteFromDevice(device: String, isGate: Boolean): String =
    if (isGate) "${DevicePage.Gateway.route}/$device" else "${DevicePage.BodySensor.route}/$device"

