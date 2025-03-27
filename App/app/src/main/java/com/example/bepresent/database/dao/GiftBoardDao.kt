package com.example.bepresent.database.dao

import androidx.room.*
import com.example.bepresent.database.room.GiftBoardRoom

interface GiftBoardDao {
    @Insert
    suspend fun insertGiftBoard(user: GiftBoardRoom)

    @Query("SELECT * FROM giftBoards")
    suspend fun getAllGiftBoard(): List<GiftBoardRoom>

    @Query("SELECT * FROM giftBoards WHERE ownerId = :userId")
    suspend fun getAllGiftBoardByUserId(userId: Int): List<GiftBoardRoom>

    @Query("DELETE FROM giftBoards")
    suspend fun deleteAllGiftBoard()
}