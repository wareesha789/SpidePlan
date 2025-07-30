package com.wish.spideplan.data.database

import androidx.room.TypeConverter
import kotlinx.datetime.LocalDateTime
import kotlinx.datetime.LocalTime
import kotlinx.datetime.LocalDate
import com.wish.spideplan.data.model.*
import kotlinx.serialization.encodeToString
import kotlinx.serialization.decodeFromString
import kotlinx.serialization.json.Json

class Converters {
    
    // LocalDateTime converters
    @TypeConverter
    fun fromLocalDateTime(value: LocalDateTime?): String? {
        return value?.toString()
    }
    
    @TypeConverter
    fun toLocalDateTime(value: String?): LocalDateTime? {
        return value?.let { LocalDateTime.parse(it) }
    }
    
    // LocalTime converters
    @TypeConverter
    fun fromLocalTime(value: LocalTime?): String? {
        return value?.toString()
    }
    
    @TypeConverter
    fun toLocalTime(value: String?): LocalTime? {
        return value?.let { LocalTime.parse(it) }
    }
    
    // LocalDate converters
    @TypeConverter
    fun fromLocalDate(value: LocalDate?): String? {
        return value?.toString()
    }
    
    @TypeConverter
    fun toLocalDate(value: String?): LocalDate? {
        return value?.let { LocalDate.parse(it) }
    }
    
    // TaskCategory converters
    @TypeConverter
    fun fromTaskCategory(value: TaskCategory): String {
        return value.name
    }
    
    @TypeConverter
    fun toTaskCategory(value: String): TaskCategory {
        return TaskCategory.valueOf(value)
    }
    
    // TaskPriority converters
    @TypeConverter
    fun fromTaskPriority(value: TaskPriority): String {
        return value.name
    }
    
    @TypeConverter
    fun toTaskPriority(value: String): TaskPriority {
        return TaskPriority.valueOf(value)
    }
    
    // RecurringType converters
    @TypeConverter
    fun fromRecurringType(value: RecurringType?): String? {
        return value?.name
    }
    
    @TypeConverter
    fun toRecurringType(value: String?): RecurringType? {
        return value?.let { RecurringType.valueOf(it) }
    }
    
    // SleepQuality converters
    @TypeConverter
    fun fromSleepQuality(value: SleepQuality): String {
        return value.name
    }
    
    @TypeConverter
    fun toSleepQuality(value: String): SleepQuality {
        return SleepQuality.valueOf(value)
    }
    
    // QuoteCategory converters
    @TypeConverter
    fun fromQuoteCategory(value: QuoteCategory): String {
        return value.name
    }
    
    @TypeConverter
    fun toQuoteCategory(value: String): QuoteCategory {
        return QuoteCategory.valueOf(value)
    }
    
    // List<String> converters for tags
    @TypeConverter
    fun fromStringList(value: List<String>): String {
        return Json.encodeToString(value)
    }
    
    @TypeConverter
    fun toStringList(value: String): List<String> {
        return try {
            Json.decodeFromString(value)
        } catch (e: Exception) {
            emptyList()
        }
    }
}