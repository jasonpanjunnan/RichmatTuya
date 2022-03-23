package com.richmat.mytuya.data.data_source

import androidx.room.*
import com.richmat.mytuya.ui.domain.model.User
import kotlinx.coroutines.flow.Flow

@Dao
interface UserDao {
    @Query("SELECT * FROM USER")
    fun getUsers(): Flow<List<User>>

    @Query("SELECT * FROM USER WHERE id = :id")
    suspend fun getUserById(id: Int): User?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertUser(user: User)

    @Delete
    suspend fun deleteUser(user: User)
}