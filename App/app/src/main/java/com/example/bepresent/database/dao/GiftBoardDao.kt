package com.example.bepresent.database.dao

import androidx.room.*
import com.example.bepresent.database.room.GiftBoardRoom

@Dao
interface GiftBoardDao {
    @Insert
    suspend fun insertGiftBoard(user: GiftBoardRoom)

    @Query("SELECT * FROM giftBoards")
    suspend fun getAllGiftBoard(): List<GiftBoardRoom>

    @Query("DELETE FROM giftBoards")
    suspend fun deleteAllGiftBoard()
}