package com.example.bepresent.database.room

import android.graphics.Bitmap
import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.PrimaryKey

@Entity(
    tableName = "gifts",
    foreignKeys = [ForeignKey(
        entity = UserRoom::class,
        parentColumns = ["giftId"],
        childColumns = ["userId"],
        onDelete = ForeignKey.CASCADE
    )]
)
class GiftRoom (
    @PrimaryKey(autoGenerate = true) val giftId: Int = 0,
    val giftName: String = "",
    val giftDescription: String = "",
    val link: String = "",
    val image: Bitmap? = null,
    val userId: Int = 0
)