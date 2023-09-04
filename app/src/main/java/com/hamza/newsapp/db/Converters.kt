package com.hamza.newsapp.db

import androidx.room.TypeConverter
import com.hamza.newsapp.models.Source

class Converters {
    @TypeConverter
    fun fromSource(source: Source): String = source.name

    @TypeConverter
    fun toSource(name: String): Source = Source(name, name)
}