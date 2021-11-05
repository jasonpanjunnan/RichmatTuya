package com.richmat.mytuya.ui.deviceSetting

import android.os.Build
import android.util.Log
import androidx.annotation.RequiresApi
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
import androidx.compose.ui.graphics.drawscope.drawIntoCanvas
import androidx.compose.ui.graphics.drawscope.rotate
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

@RequiresApi(Build.VERSION_CODES.N)
@Preview(name = "无题", showBackground = false, widthDp = 160, heightDp = 160)
@Composable
fun ShowCircle() {
    JetsurveyTheme {
        DrawArc4(centerOffset = Offset.Zero, changeOffset = { })
    }
}

@RequiresApi(Build.VERSION_CODES.N)
@Composable
fun DrawArc4(
    modifier: Modifier = Modifier,
    centerOffset: Offset,
    changeOffset: (Offset) -> Unit,
) {
    val width = 45f
    var angle by remember {
        mutableStateOf(30.0)
    }
//    val angle = 30.0

    Canvas(modifier = modifier
        .padding(25.dp)
        .fillMaxSize(), onDraw = {
        val canvasWidth = size.width
        val canvasHeight = size.height
        val largeX = canvasWidth / 2
        val largeY = canvasHeight / 2
        val radius = min(canvasWidth, canvasHeight) / 3

        val largeRadius = min(canvasWidth, canvasHeight) / 2
        val center = Offset(radius, radius)


        val pointX = largeX + largeRadius * cos(Math.toRadians(60.0))
        val pointY = largeY + largeRadius * sin(Math.toRadians(60.0))
        val littleCenter = getOffsetByAngle(angle, largeRadius, Offset(largeX, largeY))
//        val littleCenter = getOffsetByAngle(angle, largeRadius, Offset(largeX, largeY))
        drawArc(
            brush = Brush.linearGradient(colors = listOf(Color.Yellow, Color.LightGray)),
            startAngle = 60f,
            sweepAngle = -300f,
            useCenter = false,
            style = Stroke(width = width, cap = StrokeCap.Round),
            size = Size(largeRadius * 2, largeRadius * 2)
        )

        //这是调用原来canvas的地方，比如绘制文字
//        nativeCanvas
        drawIntoCanvas { canvas ->
//            canvas.nativeCanvas.drawText()
        }
        drawCircle(color = Color(0xFF6F4D1B), center = Offset(largeX, largeY),
            radius = radius)

        drawCircle(color = Color.Red, center = Offset(pointX.toFloat(), pointY.toFloat()),
            radius = width / 2, style = Stroke(width = 5f))
    })
//    var offset by remember { mutableStateOf(Offset.Zero) }
    Canvas(modifier = modifier
        .padding(25.dp)
        .pointerInput(Unit) {
            detectDragGestures { change, dragAmount ->
                val newOffset = centerOffset + dragAmount
//                        根据角度求位置
                val myAngle = atan2(newOffset.y.toDouble(), newOffset.x.toDouble()) * 180 / PI
//                val myAngle = atan2(newOffset.y.toDouble(), newOffset.x.toDouble()) * 180 / PI

//                Log.e("TAG", "DrawArc4 2222222222222: $radius, $center")
                //TODO 获取新位置之前，加一个判断，是否大于60，或者小于120
                if (!isOverAngle(myAngle)) {
                    angle = myAngle
//                    val newNewOffset = getOffsetByAngle(myAngle, radius, center)
//                    offset = newNewOffset
                    Log.e(
                        "TAG",
                        "DragGestureDemo: , angle: $myAngle, newOffset :$newOffset,offset: $centerOffset, dragAmount: $dragAmount",
                    )
                }
            }
//        detectDragGestures()
        }, onDraw = {
        val canvasWidth = size.width
        val canvasHeight = size.height
        val radius = min(canvasWidth, canvasHeight) / 2
        val center = Offset(radius, radius)
        val littleCenter = getOffsetByAngle(angle, radius, center)
        changeOffset(littleCenter)
        Log.e("TAG",
            "DrawArc4 11111111111: $angle,littleCenter :$littleCenter,offset: $centerOffset")

        drawCircle(color = Color.Black, center = littleCenter,
            radius = width / 2, style = Stroke(width = 5f))
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

    Canvas(modifier = modifier
        .fillMaxSize()
        .pointerInput(Unit) {

//            detectDragGestures { change, dragAmount ->
//                change.consumeAllChanges()
//                offset += dragAmount
//            }
            //TODO 需要监听手指抬起放下
            detectDragGestures(
                onDragStart = {
                    Log.e("TAG", "DrawArc5: onDragStart $it", )
                },
                onDragEnd = {
                    Log.e("TAG", "DrawArc5: onDragEnd $", )
                },
                onDragCancel = {
                    Log.e("TAG", "DrawArc5: onDragCancel $", )
                },
                onDrag = { change, dragAmount ->
                    change.consumeAllChanges()
                    offset += dragAmount
                }
            )
        }, onDraw = {
        val min = min(size.height, size.width)
        val padding = ringWidth / 2
        val largeRadius = min / 2 - padding
        val littleRadius = min / 3 - padding
        val r = ringWidth / 2
        val largeCenter = Offset(min / 2, min / 2)

        drawArc(
            brush = Brush.horizontalGradient(colors = listOf(Color.Yellow, Color.LightGray)),
            startAngle = 60f,
            sweepAngle = -300f,
            useCenter = false,
            style = Stroke(width = ringWidth, cap = StrokeCap.Round),
            size = Size(largeRadius * 2, largeRadius * 2),
            topLeft = Offset(padding, padding)
        )
        drawCircle(color = Color(0xFF6F4D1B), center = largeCenter,
            radius = littleRadius)

        var myAngle = atan2(offset.y.toDouble(), offset.x.toDouble()) * 180 / PI
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
        val littleRingCenter = getOffsetByAngle(myAngle, largeRadius, largeCenter)

        drawCircle(color = Color.White, center = littleRingCenter,
            radius = r, style = Stroke(width = 5f))
    })
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
