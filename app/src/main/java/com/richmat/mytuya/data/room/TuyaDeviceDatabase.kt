package com.richmat.mytuya.data.room

import androidx.room.Database
import androidx.room.RoomDatabase
import com.richmat.mytuya.data.TuyaDeviceInfoDao


//TODO 暂时没用，使用hilt会不错，先注释:注意这个exportSchema

@Database(
    version = 1,
    entities = [TuyaDeviceInfo::class], exportSchema = false
)
abstract class TuyaDeviceDatabase : RoomDatabase() {
    abstract fun tuyaDeviceDao(): TuyaDeviceInfoDao

    //使用单例防止重复创建
    //10.14 使用依赖注入之后好像就，可以不用自己写单例了
//    companion object {
//        private var instance: TuyaDeviceDatabase? = null
//
//        @Synchronized
//        fun getDatabase(context: Context): TuyaDeviceDatabase {
//            instance?.let { return it }
//            return Room.databaseBuilder(
//                context.applicationContext,
//                TuyaDeviceDatabase::class.java,
//                "device_database"
//            ).build().apply {
//                instance = this
//            }
//        }
//}

//第二种单例方式 start
//    companion object {
//        private lateinit var tuyaDeviceDatabase1: TuyaDeviceDatabase
//        fun getDataBase(context: Context): TuyaDeviceDatabase {
//            if (!this::tuyaDeviceDatabase1.isInitialized) {
//                tuyaDeviceDatabase1 = Room.databaseBuilder(
//                    context.applicationContext,
//                    TuyaDeviceDatabase::class.java,
//                    "device_database"
//                ).build()
//            }
//            return tuyaDeviceDatabase1
//        }
//    }
//end
}