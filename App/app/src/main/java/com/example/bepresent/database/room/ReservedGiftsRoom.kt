package com.example.bepresent.database.room

import android.graphics.Bitmap
import androidx.room.Entity
import androidx.room.PrimaryKey
import java.util.Date

@Entity(tableName = "reservedGifts")
data class ReservedGiftsRoom(
    @PrimaryKey(autoGenerate = true) val reservedGiftId: Int = 0,
    val reservationDate: Date? = null,

    val receiverId: Int = 0,
    val receiverName: String = "",

    val reservedGiftName: String = "",
    val reservedGiftDescription: String = "",
    val link: String = "",
    val image: Bitmap? = null,
)