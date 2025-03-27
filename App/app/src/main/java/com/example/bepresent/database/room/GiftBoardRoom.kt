package com.example.bepresent.database.room

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.PrimaryKey
import java.util.Date

@Entity(
    tableName = "giftBoards",
    foreignKeys = [ForeignKey(
        entity = UserRoom::class,
        parentColumns = ["boardId"],
        childColumns = ["ownerId"],
        onDelete = ForeignKey.CASCADE
    )]
)
class GiftBoardRoom(
    @PrimaryKey(autoGenerate = true) val boardId: Int = 0,
    val ownerId: Int = 0,
    val boardName: String = "",
    val celebrationDate: Date? = null,
    // Will not work properly with list converter, so avoid it for the first tile
    val collaborators: List<Int>? = null, // Whatever it supposed to mean???
    val accessType: String = "",
    val description: String = "",
    val creationDate: Date? = null
)