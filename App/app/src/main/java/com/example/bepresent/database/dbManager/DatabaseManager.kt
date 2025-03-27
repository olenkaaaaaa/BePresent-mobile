package com.example.bepresent.database.dbManager

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.example.bepresent.database.dao.*
import com.example.bepresent.database.room.*
import com.example.bepresent.database.typeConverters.*

@Database(entities = [UserRoom::class, GiftRoom::class, GiftBoardRoom::class, ReservationRoom::class], version = 1)
@TypeConverters(DateConverter::class, ImageConverter::class, ListConverter::class)
abstract class DatabaseManager : RoomDatabase() {
    // Used to operate with tables; functions defined within Dao's are requests to this table
    // Table structure defined within respective room class
    abstract fun userDao(): UserDao
    abstract fun giftDao(): GiftDao
    abstract fun giftBoardDao(): GiftBoardDao
    abstract fun reservationDao(): ReservationDao

    companion object {
        @Volatile private var INSTANCE: DatabaseManager? = null

        // Call this function in main to get DB
        fun getDatabase(context: Context): DatabaseManager {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    DatabaseManager::class.java,
                    "app_database"
                ).fallbackToDestructiveMigration() // Destroys and recreates DB if schema changes
                    .build()
                INSTANCE = instance
                instance
            }
        }
    }
}