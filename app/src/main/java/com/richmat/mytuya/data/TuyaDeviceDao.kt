package com.richmat.mytuya.data

import androidx.room.*
import com.richmat.mytuya.data.room.TuyaDeviceInfo
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.distinctUntilChanged

@Dao
interface TuyaDeviceInfoDao {

    @Query("SELECT * FROM tuya_device WHERE type=:type")
    fun getAll(type: Int = 1): Flow<List<TuyaDeviceInfo>>

    fun getAllDistinct() = getAll().distinctUntilChanged()

    //添加一个涂鸦设备
    @Insert
    fun insert(TuyaDeviceInfo: TuyaDeviceInfo)

    //删除一个涂鸦设备
    @Delete
    fun delete(TuyaDeviceInfo: TuyaDeviceInfo)

    @Update
    fun update(TuyaDeviceInfo: TuyaDeviceInfo)

    //使用SQL语句删除所有设备
    @Query("DELETE FROM tuya_device")
    fun deleteAll()

    //根据Id删除设备
    @Query("DELETE FROM tuya_device WHERE device_id = :id")
    fun deleteById(id: String)

}