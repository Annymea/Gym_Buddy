package com.example.GymBuddy.data.localdatabase

import androidx.room.TypeConverter
import java.util.Date

class DateTimestampCoverter {
    @TypeConverter
    fun dateToTimestamp(date: Date?): Long? {
        return date?.time
    }

    @TypeConverter
    fun timeStampToDate(value: Long?): Date? {
        return value?.let { Date(it) }
    }
}
