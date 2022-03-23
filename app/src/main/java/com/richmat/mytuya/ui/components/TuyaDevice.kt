package com.richmat.mytuya.ui.components

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material.icons.filled.Sort
import androidx.compose.material.icons.filled.Speed
import androidx.compose.ui.graphics.vector.ImageVector

data class TuyaDevice(
    val type: String,
    val name: String,
    val location: String,
    val icon: ImageVector,
    val isContent: Boolean
)

data class RoomMenu(var icon: ImageVector, var name: String)

var dropList = listOf(
    RoomMenu(Icons.Default.Speed, "宫格显示"),
    RoomMenu(Icons.Default.Sort, "设备管理"),
    RoomMenu(Icons.Default.Settings, "房间管理"),
)