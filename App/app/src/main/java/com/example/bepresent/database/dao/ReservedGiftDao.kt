package com.example.bepresent.database.dao

import androidx.room.*
import com.example.bepresent.database.room.ReservedGiftsRoom

@Dao
interface ReservedGiftDao {
    @Insert
    suspend fun insertReservedGift(reservedGifts: ReservedGiftsRoom)

    @Query("SELECT * FROM reservedGifts")
    suspend fun getAllReservedGifts(): List<ReservedGiftsRoom>

    @Query("DELETE FROM reservedGifts")
    suspend fun deleteAllReservedGifts()
}