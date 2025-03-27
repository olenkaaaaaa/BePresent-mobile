package com.example.bepresent.database.typeConverters

import androidx.room.TypeConverter
import java.util.Date

class DateConverter {
    @TypeConverter
    fun fromDate(value: Date?): Long? {
        return value?.time // Convert Date -> Long
    }

    @TypeConverter
    fun toDate(value: Long?): Date? {
        return value?.let { Date(it) } // Convert Long -> Date
    }
}