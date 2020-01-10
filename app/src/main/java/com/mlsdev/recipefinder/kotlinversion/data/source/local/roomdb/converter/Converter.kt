package com.mlsdev.recipefinder.kotlinversion.data.source.local.roomdb.converter

import androidx.room.TypeConverter
import androidx.room.util.StringUtil

class Converter {

    @TypeConverter
    fun stringListToString(stringList: List<String>?): String? {
        if (stringList == null) return null

        val listAsString: String = stringList.toString().replace(" ", "")
        return listAsString.substring(1, listAsString.length - 1)
    }

    @TypeConverter
    fun stringToStringList(string: String?): List<String>? {
        if (string == null) return null

        return string.split(",")
    }

    @TypeConverter
    fun integersListToString(ints: List<Int>?): String? {
        if (ints == null) return null

        return StringUtil.joinIntoString(ints)
    }

    @TypeConverter
    fun stringToIntegerList(string: String?): List<Int>? {
        if (string == null) return null

        return StringUtil.splitToIntList(string)
    }

}
