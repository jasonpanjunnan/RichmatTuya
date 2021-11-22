package com.richmat.mytuya.ui.deviceSurface.viewModel

import android.util.Log
import androidx.compose.ui.geometry.Offset
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject
import kotlin.math.PI
import kotlin.math.atan2
import kotlin.math.sqrt

@HiltViewModel
class BulbLightViewModel @Inject constructor(
    savedStateHandle: SavedStateHandle,
) : ViewModel() {

    private val _radianState = MutableStateFlow(0.0)
    val radianState: StateFlow<Double> = _radianState

    fun changeRadian(newRadian: Double, percent: Float) {
        _radianState.value = newRadian
        val bright = percent * 1000
        viewModelScope.launch {
            Log.e("TAG", "changeRadian: $bright，$percent")
        }
    }

    init {
        //联网获取。x轴代表冷暖值，两边统一一下就可以了.弧度不是这么算的，勾股定理吧.
        val brightPercent = 50 //0 - 1000
        val tempPercent = 0.8f // 10 - 1000

        //其实就相当于半径是500的圆，因为无法确定实际圆的大小，所以需要先转成弧度使用
        val x = brightPercent - 500
        val radius = 500
        val y = sqrt((radius * radius - x * x).toDouble())
        _radianState.value = -Math.toRadians(atan2(y, x.toDouble()) * 180 / PI)
//        _radianState.value = (brightPercent / 500) * (1 / PI)
    }
}