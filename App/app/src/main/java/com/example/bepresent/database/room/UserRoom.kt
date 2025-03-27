package com.example.bepresent.database.room

import androidx.room.Entity
import androidx.room.PrimaryKey
import java.util.Date

@Entity(tableName = "users")
data class UserRoom (
    @PrimaryKey(autoGenerate = true) val userId: Int = 0,
    val username: String = "",
    val email: String = "",
    val password: String = "",
    val dataOfBirth: Date? = null,
    val gender: String = "Gremlin",
    val interests: List<String>? = null,
    // val isAuthorized: Boolean = false
)