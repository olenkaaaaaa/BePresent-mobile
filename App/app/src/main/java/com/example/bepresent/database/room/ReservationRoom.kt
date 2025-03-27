package com.example.bepresent.database.room

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.PrimaryKey
import java.util.Date

@Entity(
    tableName = "reservation",
    foreignKeys = [
        ForeignKey(
            entity = UserRoom::class,
            parentColumns = ["reservationId"],
            childColumns = ["giftId"],
            onDelete = ForeignKey.CASCADE),
        ForeignKey(
            entity = GiftRoom::class,
            parentColumns = ["reservationId"],
            childColumns = ["giftId"],
            onDelete = ForeignKey.CASCADE
        )]
)
class ReservationRoom(
    @PrimaryKey(autoGenerate = true) val reservationId: Int = 0,
    val giftId: Int = 0,
    val userId: Int = 0,
    val reservationDate: Date? = null,
)