package com.richmat.mytuya.data.room

import androidx.room.ColumnInfo
import androidx.room.ColumnInfo.TEXT
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "tuya_device")
data class TuyaDeviceInfo(
    @PrimaryKey(autoGenerate = true) var id: Long,
    var deviceName: String,
    @ColumnInfo(name = "device_id", typeAffinity = TEXT)
    var deviceId: String,
    @ColumnInfo(name = "type", typeAffinity = ColumnInfo.INTEGER)
    var type: Int
)