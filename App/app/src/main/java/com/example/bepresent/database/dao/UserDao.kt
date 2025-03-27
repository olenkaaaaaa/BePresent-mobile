package com.example.bepresent.database.dao

import androidx.room.*
import com.example.bepresent.database.room.UserRoom

@Dao
interface UserDao {
    @Insert
    suspend fun insertUser(user: UserRoom)

    @Query("SELECT * FROM users")
    suspend fun getAllUsers(): List<UserRoom>

    @Query("DELETE FROM users")
    suspend fun deleteAllUsers()
}