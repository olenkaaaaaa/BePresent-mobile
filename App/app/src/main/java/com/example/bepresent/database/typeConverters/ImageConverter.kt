package com.example.bepresent.database.typeConverters

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import androidx.room.TypeConverter
import java.io.ByteArrayOutputStream

// Converts images (as bitmap) to byte array to store in db (Blob)
class ImageConverter {
    @TypeConverter
    fun fromBitmap(value: Bitmap): ByteArray{
        val stream = ByteArrayOutputStream()
        value.compress(Bitmap.CompressFormat.PNG, 100, stream)
        return stream.toByteArray()
    }

    @TypeConverter
    fun toBitMap(value: ByteArray): Bitmap{
        return BitmapFactory.decodeByteArray(value, 0, value.size)
    }
}