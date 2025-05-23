package com.example.bepresent.database.dbManager

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import androidx.room.migration.Migration
import androidx.sqlite.db.SupportSQLiteDatabase
import com.example.bepresent.database.dao.*
import com.example.bepresent.database.room.*
import com.example.bepresent.database.typeConverters.*

@Database(
    entities = [GiftRoom::class, GiftBoardRoom::class, ReservedGiftsRoom::class],
    version = 3,
    exportSchema = false
)
@TypeConverters(TypeConverter::class)
abstract class DatabaseManager : RoomDatabase() {
    // Used to operate with tables; functions defined within Dao's are requests to this table
    // Table structure defined within respective room class
    abstract fun giftDao(): GiftDao
    abstract fun giftBoardDao(): GiftBoardDao
    abstract fun reservedGiftsDao(): ReservedGiftDao

    companion object {
        @Volatile
        private var instance: DatabaseManager? = null

        // Migration from version 2 to 3
        private val MIGRATION_2_3 = object : Migration(2, 3) {
            override fun migrate(database: SupportSQLiteDatabase) {
                // Add any schema changes here if needed
                // For now, no changes needed
            }
        }

        // Call this function in main to get DB
        fun getDatabase(context: Context): DatabaseManager =
            instance ?: synchronized(this) {
                instance ?: buildDatabase(context).also { instance = it }
            }

        // Create new Local Database if don't have one
        private fun buildDatabase(context: Context) =
            Room.databaseBuilder(
                context.applicationContext,
                DatabaseManager::class.java,
                "LocalDatabase.db"
            )
                .addMigrations(MIGRATION_2_3)
                .fallbackToDestructiveMigration() // Для розробки - в продакшені краще використовувати proper migrations
                .build()
    }
}