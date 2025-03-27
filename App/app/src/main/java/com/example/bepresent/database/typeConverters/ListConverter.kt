package com.example.bepresent.database.typeConverters

import androidx.room.TypeConverter
import org.json.JSONArray

class ListConverter {
    @TypeConverter
    fun fromList(value: List<String>): String {
        return JSONArray(value).toString() // Convert List<String> -> JSON String
    }

    @TypeConverter
    fun toList(value: String): List<String> {
        val jsonArray = JSONArray(value) // Convert JSON String -> JSONArray
        val list = mutableListOf<String>()

        for (i in 0 until jsonArray.length()) {
            list.add(jsonArray.getString(i)) // Extract each string and add to list
        }
        return list
    }
}