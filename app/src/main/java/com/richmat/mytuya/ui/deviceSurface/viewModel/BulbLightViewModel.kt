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
}