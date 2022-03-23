package com.richmat.mytuya.data.data_source

import androidx.room.Database
import androidx.room.RoomDatabase
import com.richmat.mytuya.ui.domain.model.User

@Database(
    version = 1,
    exportSchema = false,
    entities = [User::class]
)
abstract class UserDataBase : RoomDatabase() {
    abstract val userDao: UserDao

    companion object {
        const val DATABASE_NAME = "notes_db"
    }
}