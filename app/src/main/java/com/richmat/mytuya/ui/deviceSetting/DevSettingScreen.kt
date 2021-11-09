package com.richmat.mytuya.ui.deviceSetting

import android.util.Log
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.gestures.detectDragGestures
import androidx.compose.foundation.layout.*
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ChevronLeft
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Rect
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.*
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.graphics.drawscope.rotate
import androidx.compose.ui.graphics.drawscope.translate
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.input.pointer.consumeAllChanges
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.rememberImagePainter
import com.example.compose.jetsurvey.theme.JetsurveyTheme
import com.example.compose.jetsurvey.theme.Red800
import com.richmat.mytuya.R
import com.richmat.mytuya.ui.components.DevPicAndName
import com.richmat.mytuya.ui.deviceSurface.getOffsetByAngle
import com.richmat.mytuya.ui.deviceSurface.getOffsetByRadian
import com.richmat.mytuya.ui.deviceSurface.getOffsetByRadians
import com.richmat.mytuya.ui.deviceSurface.isOverAngle
import com.richmat.mytuya.ui.searchResult.RenameDialog
import kotlin.math.*

@Composable
fun DevSetting(viewModel: DevSettingViewModel, onBack: () -> Unit) {
    val state by viewModel.uiState.collectAsState()
    var showDialog by remember {
        mutableStateOf(false)
    }

    DevSettingScreen(
        onBack,
        rememberImagePainter(data = state.iconUri, builder = {
            crossfade(true)
            placeholder(drawableResId = R.drawable.gate)
        }),
        state.devName,
        { showDialog = true },
        {}
    )

    if (showDialog) {
        RenameDialog(value = state.devName,
            onValueChange = { viewModel.changeName(it) },
            onDismiss = { showDialog = false },
            rename = { viewModel.rename() })
    }
}

//TODO 整完这个，把改名的dialog整整
@Composable
fun DevSettingScreen(
    onBack: () -> Unit,
    painter: Painter,
    devName: String,
    showDetail: () -> Unit,
    onWidgetClick: () -> Unit,
) {
    Column {
        TopAppBar(navigationIcon = {
            IconButton(onClick = onBack) {
                Icon(imageVector = Icons.Filled.ChevronLeft, contentDescription = null)
            }
        }, title = {})
        DevPicAndName(
            painter = painter,
            name = devName,
            onWidgetClick = onWidgetClick,
            showDetail = showDetail,
            subtitle = "",
            modifier = Modifier.background(Color.White)
        )
        Divider(modifier = Modifier
            .height(5.dp)
            .background(Color.White.copy(alpha = 0.9f)))
        DrawLine(modifier = Modifier.weight(1f))
        Divider(modifier = Modifier
            .height(5.dp)
            .background(Color.White.copy(alpha = 0.9f)))
        Box(contentAlignment = Alignment.Center, modifier = Modifier.fillMaxWidth()
//            .fillMaxSize()
//            .wrapContentSize(Alignment.Center)
//            .weight(1f)
        ) {
            TextButton(onClick = { /*TODO*/ }) {
                Text(text = "移除设备", color = Red800, fontSize = 20.sp)
            }
        }
//        Row() {
//            Image(painter = painter,
//                contentDescription = null,
//                modifier = Modifier
//                    .size(75.dp)
//                    .padding(5.dp))
//            Column() {
//
//            }
//        }
    }
}

@Preview(showBackground = true, widthDp = 160, heightDp = 160)
@Composable
fun DrawLine(modifier: Modifier = Modifier) {
    Canvas(modifier = modifier.fillMaxSize(), onDraw = {
        val canvasWidth = size.width
        val canvasHeight = size.height

        drawLine(
            start = Offset(0f, 0f),
            end = Offset(canvasWidth, canvasHeight),
            color = Color.Green,
            strokeWidth = 10f
        )
    })
}

@Preview
@Composable
fun ShowArc5() {
    JetsurveyTheme {
        DrawArc5(modifier = Modifier.size(260.dp), initAngle = 30.0,
            ringWidth = 45f)
    }
}

@Composable
fun DrawArc5(modifier: Modifier = Modifier, initAngle: Double, ringWidth: Float) {

    var offset by remember {
        mutableStateOf(Offset.Zero)
    }

    var myAngle by remember {
        mutableStateOf(60.0)
    }

    Canvas(modifier = modifier
        .fillMaxSize()
        .pointerInput(Unit) {
            detectDragGestures { change, dragAmount ->
                Log.e("TAG", "DrawArc5: $change $size,")
                val newOffset = offset + dragAmount
                val min = min(size.height, size.width)
                val padding = ringWidth / 2
                val largeRadius = min / 2 - padding
                if (newOffset.getDistance() > largeRadius) return@detectDragGestures

                change.consumeAllChanges()
                offset = newOffset
                myAngle = atan2(offset.y, offset.x) * 180 / PI
                val radian = Math.toRadians(myAngle)
                val littleRingCenter = getOffsetByRadian(radian, largeRadius.toFloat(), Offset.Zero)
//                val littleRingCenter = getOffsetByAngle(myAngle, largeRadius, Offset.Zero)
            }
            //TODO 需要监听手指抬起放下
//            detectDragGestures(
//                onDragStart = {
//                    Log.e("TAG", "DrawArc5: onDragStart $it")
//                },
//                onDragEnd = {
//                    Log.e("TAG", "DrawArc5: onDragEnd $")
//                },
//                onDragCancel = {
//                    Log.e("TAG", "DrawArc5: onDragCancel $")
//                },
//                onDrag = { change, dragAmount ->
//                    change.consumeAllChanges()
//                    offset += dragAmount
//                }
//            )
        }, onDraw = {
        val min = size.minDimension
        val padding = ringWidth / 2
        val largeRadius = min / 2 - padding

        drawArc(
            brush = Brush.horizontalGradient(colors = listOf(Color.Yellow, Color.LightGray)),
            startAngle = 60f,
            sweepAngle = -300f,
            useCenter = false,
            style = Stroke(width = ringWidth, cap = StrokeCap.Round),
            size = Size(largeRadius * 2, largeRadius * 2),
            topLeft = Offset(padding, padding)
        )
        val minRadius = size.minDimension / 2
        translate(left = minRadius, top = minRadius) {
            val min = size.minDimension
            val padding = ringWidth / 2
            val largeRadius = min / 2 - padding
            val littleRadius = min / 3 - padding
            val r = ringWidth / 2
//        val largeCenter = Offset(min / 2, min / 2)

            drawCircle(color = Color(0xFF6F4D1B), center = Offset.Zero,
                radius = littleRadius)

            //TODO 懂了懂了，开始的位置是Offset（0,0），所以滑动是从中心开始的，的想办法，根据角度更新一下位置。对对对吗？
            //TODO 不对不对，原来atan2一直都是以左上角为圆心的，原来跟着动是因为画布的关系，那怎么办呢？？？

            //TODO 这块也应该放到手势里边，不然可能不好处理
            if (isOverAngle(myAngle)) {
                when {
                    myAngle < 90.0 -> {
                        myAngle = 60.0
                    }
                    myAngle > 90.0 -> {
                        myAngle = 120.0
                    }
                }
            }
            val littleRingCenter = getOffsetByAngle(myAngle, largeRadius, Offset.Zero)

            drawCircle(color = Color.White, center = littleRingCenter,
                radius = r, style = Stroke(width = 5f))

            drawPoints(points = listOf(offset),
                color = Color.Black,
                pointMode = PointMode.Points,
                strokeWidth = 20f)
        }
    })
}

@Preview(name = "DrawCircle4")
@Composable
fun Show() {
    DrawCircle4(width = 5f, maxRadius = 30f)
}

/**
 * 第二种思路
 * ,可以准备功成身退了
 */
@Composable
fun DrawCircle4(modifier: Modifier = Modifier, width: Float, maxRadius: Float) {
    //获取圆环实际半径
    val radius = maxRadius - width / 2
    var myCenter by remember {
        mutableStateOf(Offset.Zero)
    }

    var offset by remember {
        mutableStateOf(Offset.Zero)
    }

    Canvas(modifier = modifier
        .fillMaxSize()
        .pointerInput(Unit) {
            detectDragGestures { change, dragAmount ->
                val newOffset = offset + dragAmount
                val largeRadius = min(size.width, size.height)
                if (newOffset.x.absoluteValue > largeRadius || newOffset.y.absoluteValue > largeRadius) return@detectDragGestures
                offset = newOffset
                val myAngle = atan2(offset.y, offset.x) * 180 / PI
                val radian = Math.toRadians(myAngle)
                val littleRingCenter = getOffsetByRadian(radian, largeRadius.toFloat(), Offset.Zero)
//                val littleRingCenter = getOffsetByAngle(myAngle, largeRadius.toFloat(), Offset.Zero)
                myCenter = littleRingCenter
            }
        }, onDraw = {
        val minRadius = size.minDimension / 2
        translate(left = minRadius, top = minRadius) {
            drawCircle(Color.Red, radius = radius, style = Stroke(width = width), center = myCenter)

            drawPoints(points = listOf(offset),
                color = Color.Black,
                pointMode = PointMode.Points,
                strokeWidth = 20f)
        }
    })
}

//不用管那么多，只要你是个环就行了，反正我是offset的，有点绕，但是有道理，有道理个屁

fun fixOffset(offset: Offset, radius: Float, littleRingCenter: Offset): Offset {
    val x = littleRingCenter.x - radius
    val y = littleRingCenter.y - radius
    return Offset(x, y)
}

@Preview(showBackground = true, widthDp = 160, heightDp = 160)
@Composable
fun DrawArc(modifier: Modifier = Modifier) {
    Canvas(modifier = modifier.fillMaxSize(), onDraw = {
        val canvasWidth = size.width
        val canvasHeight = size.height
        drawArc(
            //TODO 最想原版的，可以根据x轴占得百分比获取颜色。可能还是比不上直接拿
            brush = Brush.horizontalGradient(colors = listOf(Color.LightGray,
                Color.Yellow)),
            startAngle = 60f,
            sweepAngle = -300f,
            useCenter = false, style = Stroke(width = 55f, cap = StrokeCap.Round)
        )
    })
}

@Preview(showBackground = true, widthDp = 160, heightDp = 160)
@Composable
fun DrawArc2(modifier: Modifier = Modifier) {
    Canvas(modifier = modifier.fillMaxSize(), onDraw = {
        val canvasWidth = size.width
        val canvasHeight = size.height

        drawArc(
            brush = Brush.linearGradient(colors = listOf(Color.Blue, Color.Cyan, Color.LightGray)),
            startAngle = 0f,
            sweepAngle = 270f,
            useCenter = true, style = Stroke(width = 10f, cap = StrokeCap.Round)
        )
    })
}

@Preview(showBackground = true, widthDp = 160, heightDp = 160)
@Composable
fun DrawArc3(modifier: Modifier = Modifier) {
    Canvas(modifier = modifier.fillMaxSize(), onDraw = {
        val canvasWidth = size.width
        val canvasHeight = size.height

        drawArc(
            color = Color(0xFFFF3D00),
            startAngle = 0f,
            sweepAngle = 270f,
            useCenter = false, style = Stroke(width = 20f, cap = StrokeCap.Round)
        )
//        atan2()
//        y=sqrt(160.0.pow(2.0).toFloat() - ((point.x - 10).toDouble()).pow(2.0)).toFloat() + 10
    })
}

@Preview(showBackground = true, widthDp = 160, heightDp = 160)
@Composable
fun DrawPath(modifier: Modifier = Modifier) {
    Canvas(modifier = modifier, onDraw = {
        val canvasWidth = size.width
        val canvasHeight = size.height
        val path = Path()
        path.addArc(oval = Rect(0f, 0f, canvasWidth, canvasHeight), 0f, 180f)
        drawPath(
            path = path,
            brush = Brush.linearGradient(colors = listOf(Color.Blue, Color.Cyan, Color.LightGray))
        )
    })
}

@Preview(showBackground = true, widthDp = 160, heightDp = 160)
@Composable
fun DrawRotatePath(modifier: Modifier = Modifier) {
    Canvas(modifier = modifier, onDraw = {
        val canvasWidth = size.width
        val canvasHeight = size.height
        val path = Path()
        path.addArc(oval = Rect(0f, 0f, canvasWidth, canvasHeight),
            0f, 180f)
        rotate(degrees = 90f, pivot = center) {
            drawPath(
                path = path,
                brush = Brush.linearGradient(colors = listOf(Color.Blue,
                    Color.Cyan,
                    Color.LightGray))
            )
        }
    })
}


@Preview
@Composable
fun ShowDevSetting() {
    JetsurveyTheme {
        DevSettingScreen(
            onBack = {},
            painter = painterResource(id = R.drawable.gate_image),
            devName = "网关zigbe 1122 2222 2222",
            showDetail = {},
            onWidgetClick = {}
        )
    }
}
