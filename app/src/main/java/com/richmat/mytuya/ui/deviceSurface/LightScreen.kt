package com.richmat.mytuya.ui.deviceSurface

import android.os.Build
import android.util.Log
import androidx.annotation.RequiresApi
import androidx.compose.foundation.layout.*
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Backpack
import androidx.compose.material.icons.filled.Power
import androidx.compose.material.icons.filled.Settings
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Alignment.Companion.CenterEnd
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.AlignmentLine
import androidx.compose.ui.layout.FirstBaseline
import androidx.compose.ui.layout.Layout
import androidx.compose.ui.layout.layout
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.compose.jetsurvey.theme.JetsurveyTheme
import com.richmat.mytuya.R
import com.richmat.mytuya.ui.deviceSetting.DrawArc4
import com.richmat.mytuya.ui.deviceSetting.DrawArc5
import com.richmat.mytuya.ui.deviceSurface.viewModel.BulbLightViewModel
import kotlin.math.*


@RequiresApi(Build.VERSION_CODES.N)
@Composable
fun BulbLight(back: () -> Unit, setting: () -> Unit, viewModel: BulbLightViewModel) {
    val offset by viewModel.offsetState.collectAsState()
    BulbLightScreen(back = back, setting = setting, powerOff = {}, offset = offset,
        changeOffset = { viewModel.changeOffset(it) })
}

@RequiresApi(Build.VERSION_CODES.N)
@Composable
fun BulbLightScreen(
    modifier: Modifier = Modifier,
    back: () -> Unit,
    deviceName: String = "",
    setting: () -> Unit,
    powerOff: () -> Unit,
    offset: Offset,
    changeOffset: (Offset) -> Unit,
) {
    Scaffold(
        topBar = {
            TopAppBar(
                navigationIcon = {
                    IconButton(onClick = back) {
                        Icon(imageVector = Icons.Default.Backpack, contentDescription = "")
                    }
                }, title = {
                    Text(text = deviceName)
                },
                actions = {
                    IconButton(onClick = setting) {
                        Icon(imageVector = Icons.Default.Settings, contentDescription = "")
                    }
                })
        },
        bottomBar = {
            BottomAppBar(
                modifier = Modifier
                    .fillMaxWidth()
            ) {
                Box() {
                    IconButton(
                        onClick = powerOff,
                        modifier = Modifier
                            .fillMaxWidth()
//                            .align(CenterVertically)
                    ) {
                        Icon(imageVector = Icons.Default.Power, contentDescription = "")
                    }

                    TextButton(onClick = { /*TODO*/ }, modifier = Modifier.align(
                        CenterEnd)) {
                        Text(stringResource(id = R.string.timing))
                    }
                }

            }
        }

    ) {
        Box(modifier = modifier, contentAlignment = Alignment.Center) {
            Column {
                DragGestureDemo(offset, changeOffset)
                LightPicker()
            }
        }
    }
}

/**
 * 实现思路：
 * 1.根据移动的位置坐标 获取角度 然后根据角度获取在圆环上的位置
 */
@RequiresApi(Build.VERSION_CODES.N)
//@Preview(o)
@Composable
fun DragGestureDemo(offset: Offset, changeOffset: (Offset) -> Unit) {
//    var boxSize = 100.dp
//    var offset by remember { mutableStateOf(Offset.Zero) }
    Box(contentAlignment = Alignment.Center,
        modifier = Modifier
            .wrapContentSize()
            .fillMaxSize()
    ) {
//        Log.e("TAG", "DragGestureDemo: ${Offset.Zero}")
//        Box(Modifier
//            .size(boxSize)
//            .offset {
//                IntOffset(offset.x.roundToInt(), offset.y.roundToInt())
//            }
//            .background(Color.Green)
//            .pointerInput(Unit) {
//                detectDragGestures(
//                    onDragStart = { offset ->
//                        // 拖动开始
//                    },
//                    onDragEnd = {
//                        // 拖动结束
//                    },
//                    onDragCancel = {
//                        // 拖动取消
//                    },
//                    onDrag = { change: PointerInputChange, dragAmount: Offset ->
//                        // 拖动中
//                        val newOffset = offset + dragAmount
////                        val angle =
////                            getAngle(newOffset.x.toDouble(), newOffset.y.toDouble(), Offset.Zero)
//
////                        根据角度求位置
//                        val angle = atan2(newOffset.y.toDouble(), newOffset.x.toDouble()) * 180 / PI
//                        //TODO 获取新位置之前，加一个判断，是否大于60，或者小于120
//                        if (!isOverAngle(angle)){
//                            val newNewOffset = getOffsetByAngle(angle, radius, Offset.Zero)
//                            offset = newNewOffset
//                            Log.e(
//                                "TAG",
//                                "DragGestureDemo: $offset, new: $newOffset, newnew: $newNewOffset, angle: $angle",
//                            )
//                        }
//                        //根据弧度求位置
////                        val angle = atan2(newOffset.y.toDouble(), newOffset.x.toDouble())
////                        val newNewOffset = getOffsetByRadians(angle, radius, Offset.Zero)
////                            offset += dragAmount
//                    }
//                )
//            }
//        )
//        DrawArc4(Modifier.size(260.dp), offset, changeOffset)
        DrawArc5(Modifier.size(260.dp), initAngle = 30.0,
            ringWidth = 120f)
    }
}

fun isOverAngle(angle: Double): Boolean = (angle > 60.0 && angle < 120)

fun getOffsetByAngle(angle: Double, radius: Float, center: Offset): Offset {
    val pointX = center.x + radius * cos(Math.toRadians(angle))
    val pointY = center.y + radius * sin(Math.toRadians(angle))
    return Offset(pointX.toFloat(), pointY.toFloat())
}

fun getOffsetByRadians(angle: Double, radius: Float, center: Offset): Offset {
    val pointX = center.x + radius * cos(angle)
    val pointY = center.y + radius * sin(angle)
    return Offset(pointX.toFloat(), pointY.toFloat())
}

private fun getAngle(xTouch: Double, yTouch: Double, center: Offset): Double {
    val x = xTouch - center.x
    val y = yTouch - center.y
    return (asin(y / hypot(x, y)) * 180 / Math.PI)
}

fun Modifier.Light_Palette(): Modifier {
    return layout { measurable, constraints ->
        val placeable = measurable.measure(constraints = constraints)
//        placeable[]
        check(placeable[FirstBaseline] != AlignmentLine.Unspecified)
        val placeable1 = measurable
        layout(0, 0) {}
    }
}

fun Modifier.new_Light_Palette() =
    this.layout { measurable, constraints ->
        layout(0, 0) {}
    }

@Composable
fun Text(
    modifier: Modifier = Modifier
        .new_Light_Palette()
        .fillMaxWidth()
        .Light_Palette(),
) {

    Layout(content = { /*TODO*/ }) { measures, constraints ->
        measures.mapIndexed { index, measurable ->

        }
        layout(1, 2) {}
    }
}

@Composable
fun LightPicker() {
    var process by remember {
        mutableStateOf(0.5f)
    }
    Slider(
        value = process,
        onValueChange = {
            process = it
        },
        colors = SliderDefaults.colors(
            thumbColor = Color.White,
            activeTickColor = Color(0xFFFF5722)
        )
    )
}

@Preview
@Composable
fun showLightPicker() {
    JetsurveyTheme {
        LightPicker()
    }
}

@RequiresApi(Build.VERSION_CODES.N)
@Preview
@Composable
fun showLightPicker11() {
    JetsurveyTheme {
        BulbLightScreen(back = {},
            setting = {},
            powerOff = {},
            offset = Offset.Zero,
            changeOffset = {})
    }
}

///**
// * 颜色选择器
// */
//@Composable
//fun ColorPicker(onColorSelected: (Color) -> Unit) {
//    val screenWidth = LocalConfiguration.current.screenWidthDp.dp
//    val screenWidthInPx = with(LocalDensity.current) { screenWidth.toPx() }
//    var activeColor by remember { mutableStateOf(Red) }
//
//    val max = screenWidth - 16.dp
//    val min = 0.dp
//    val (minPx, maxPx) = with(LocalDensity.current) { min.toPx() to max.toPx() }
//    val dragOffset = remember { mutableStateOf(0f) }
//    Box(modifier = Modifier.padding(8.dp)) {
//        //slider
//        Spacer(
//            modifier = Modifier
//                .height(10.dp)
//                .fillMaxWidth()
//                .clip(RoundedCornerShape(4.dp))
//                .background(brush = colorMapGradient(screenWidthInPx))
//                .align(Alignment.Center)
//                .pointerInput("painter") {
//                    detectTapGestures { offset ->
//                        dragOffset.value = offset.x
//                        activeColor = getActiveColor(dragOffset.value, screenWidthInPx)
//                        onColorSelected.invoke(activeColor)
//                    }
//                }
//        )
//        // draggable icon
//        Icon(
//            imageVector = Icons.Filled.FiberManualRecord,
//            tint = activeColor,
//            contentDescription = null,
//            modifier = Modifier
//                .offset { IntOffset(dragOffset.value.roundToInt(), 0) }
//                .border(
//                    border = BorderStroke(4.dp, MaterialTheme.colors.onSurface),
//                    shape = CircleShape
//                )
//                .draggable(
//                    orientation = Orientation.Horizontal,
//                    state = rememberDraggableState { delta ->
//                        val newValue = dragOffset.value + delta
//                        dragOffset.value = newValue.coerceIn(minPx, maxPx)
//                        activeColor = getActiveColor(dragOffset.value, screenWidthInPx)
//                        onColorSelected.invoke(activeColor)
//                    }
//                )
//        )
//    }
//}
//
//fun colorMapGradient(screenWidthInPx: Float) = Brush.horizontalGradient(
//    colors = createColorMap(),
//    startX = 0f,
//    endX = screenWidthInPx
//)
//
//fun createColorMap(): List<Color> {
//    val colorList = mutableListOf<Color>()
//    for (i in 0..360 step (10)) {
//        val randomSaturation = 90 + Random.nextFloat() * 10
//        val randomLightness = 50 + Random.nextFloat() * 10
//        val hsv = AndroidColor.HSVToColor(
//            floatArrayOf(
//                i.toFloat(),
//                randomSaturation,
//                randomLightness
//            )
//        )
//        colorList.add(Color(hsv))
//    }
//
//    return colorList
//}
//
//fun getActiveColor(dragPosition: Float, screenWidth: Float): Color {
//    val hue = (dragPosition / screenWidth) * 360f
//    val randomSaturation = 90 + Random.nextFloat() * 10
//    val randomLightness = 50 + Random.nextFloat() * 10
//    return Color(
//        AndroidColor.HSVToColor(
//            floatArrayOf(
//                hue,
//                randomSaturation,
//                randomLightness
//            )
//        )
//    )
//}