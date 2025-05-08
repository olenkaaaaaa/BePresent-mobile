package com.example.bepresent.database.room

import android.graphics.Bitmap
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "gifts")
data class GiftRoom (
    @PrimaryKey(autoGenerate = true) val giftId: Int = 0,
    val giftName: String = "",
    val giftDescription: String = "",
    val link: String = "",
    val image: Bitmap? = null,
    val reserved: Boolean = false,
    val reservoirUserId: Int? = null
)