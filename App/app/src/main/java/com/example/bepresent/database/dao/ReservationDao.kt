package com.example.bepresent.database.dao

import androidx.room.*
import com.example.bepresent.database.room.ReservationRoom

interface ReservationDao {
    @Insert
    suspend fun insertReservation(relation: ReservationRoom)

    @Query("SELECT * FROM reservation")
    suspend fun getAllReservations(): List<ReservationRoom>

    @Query("DELETE FROM reservation")
    suspend fun deleteAllReservations()
}