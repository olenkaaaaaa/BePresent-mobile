package com.example.bepresent.database.dao

import androidx.room.*
import com.example.bepresent.database.room.GiftRoom

@Dao
interface GiftDao {
    @Insert
    suspend fun insertGift(gift: GiftRoom)

    @Query("SELECT * FROM gifts")
    suspend fun getAllGifts(): List<GiftRoom>

    @Query("SELECT * FROM gifts WHERE userId = :userId")
    suspend fun getAllGiftsByUserId(userId: Int): List<GiftRoom>

    @Query("DELETE FROM gifts")
    suspend fun deleteAllGifts()
}