package com.example.bepresent.database.room

import androidx.room.Entity
import androidx.room.PrimaryKey
import java.util.Date

@Entity(tableName = "giftBoards")
data class GiftBoardRoom(
    @PrimaryKey(autoGenerate = true) val boardId: Int = 0,
    val boardName: String = "",
    val celebrationDate: Date? = null,
    val accessType: String = "",
    val description: String = "",
    val creationDate: Date? = null
)