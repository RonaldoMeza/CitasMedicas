package com.citasmedicas.data.local.converter

import androidx.room.TypeConverter

class Converters {
    @TypeConverter
    fun listToString(value: List<String>?): String? = value?.joinToString("|")

    @TypeConverter
    fun stringToList(value: String?): List<String> = value?.split("|")?.filter { it.isNotBlank() } ?: emptyList()
}
