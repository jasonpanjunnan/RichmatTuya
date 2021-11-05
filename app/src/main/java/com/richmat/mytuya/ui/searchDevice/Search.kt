package com.richmat.mytuya.ui.searchDevice

import androidx.compose.runtime.Stable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf

sealed class Search {
    data class Wifi(val undefined: String) : Search()
    data class GetToken(val undefined: String) : Search()
    data class ConnectSurface(val undefined: String) : Search()
}

@Stable
class SearchSurfaceState(
    val showDone: Boolean,
    val showPrevious: Boolean,
    val searchIndex: Int,
    val searchDetail: SearchDetail,

    ) {
    val enableNext by mutableStateOf(false)
}

sealed class ConnectStep {
    data class ConnectFirst(val value: Float = 0f, val step: String) : ConnectStep()
    data class ConnectSecond(val value: Float = 0.5f, val step: String) : ConnectStep()
    data class ConnectSuccess(val value: Float = 1f, val step: String) : ConnectStep()
}

const val DEVICE_FIND = "device_find"
const val DEVICE_BIND_SUCCESS = "device_bind_success"
const val DEVICE_SEARCH_FINISH = "device_search_finish"

data class SearchStep(val step: String, val data: Any?) {
    fun getConnectStep(): ConnectStep = when (step) {
        DEVICE_FIND -> {
            ConnectStep.ConnectFirst(step = step)
        }
        DEVICE_BIND_SUCCESS -> {
            ConnectStep.ConnectSecond(step = step)
        }
        DEVICE_SEARCH_FINISH -> {
            ConnectStep.ConnectSuccess(step = step)
        }
        else -> ConnectStep.ConnectFirst(step = "")
    }
}

data class SearchDetail(
    val id: Int,
    val reminder: String,
    val secondRemider: String,
    val search: Search,
)

//这部分写的比较早，有点混乱
val searchDetails = listOf(
    SearchDetail(
        id = 1,
        reminder = "",
        secondRemider = "",
        Search.Wifi("")
    ),
    SearchDetail(
        id = 2,
        reminder = "",
        secondRemider = "",
        Search.GetToken("")
    ),
    SearchDetail(
        id = 3,
        reminder = "",
        secondRemider = "",
        Search.ConnectSurface("")
    ),
)

sealed class SearchResult {
    object Success : SearchResult()
    object Fail : SearchResult()
    object NoSearch : SearchResult()
}