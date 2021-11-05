package com.richmat.mytuya.ui.deviceSurface.viewModel

import androidx.compose.ui.geometry.Offset
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import javax.inject.Inject

@HiltViewModel
class BulbLightViewModel @Inject constructor(
    savedStateHandle: SavedStateHandle,
) : ViewModel() {
    private val _offsetState = MutableStateFlow(Offset.Zero)
    val offsetState: StateFlow<Offset> = _offsetState

    fun changeOffset(newOffset: Offset) {
        _offsetState.value = newOffset
    }

    init {
        //联网获取
        val brightPercent = 800 //0 - 1000
        val tempPercent = 0.8f // 10 - 1000

        _offsetState.value = Offset(brightPercent.toFloat(), 1000f)
    }
}