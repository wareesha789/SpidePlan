package com.wish.spideplan.data.model

import androidx.room.Entity
import androidx.room.PrimaryKey
import kotlinx.datetime.LocalDateTime
import kotlinx.datetime.LocalTime
import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Entity(tableName = "sleep_entries")
@Parcelize
data class SleepEntry(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,
    val bedTime: LocalTime,
    val wakeTime: LocalTime,
    val sleepQuality: SleepQuality,
    val notes: String = "",
    val date: kotlinx.datetime.LocalDate,
    val createdAt: LocalDateTime
) : Parcelable {
    // Calculate sleep duration in minutes
    val durationMinutes: Int
        get() {
            val bedTimeMinutes = bedTime.hour * 60 + bedTime.minute
            val wakeTimeMinutes = wakeTime.hour * 60 + wakeTime.minute
            
            return if (wakeTimeMinutes >= bedTimeMinutes) {
                wakeTimeMinutes - bedTimeMinutes
            } else {
                // Sleep crossed midnight
                (24 * 60) - bedTimeMinutes + wakeTimeMinutes
            }
        }
    
    val durationHours: Double
        get() = durationMinutes / 60.0
}

enum class SleepQuality(val displayName: String, val value: Int) {
    EXCELLENT("Excellent", 5),
    GOOD("Good", 4),
    FAIR("Fair", 3),
    POOR("Poor", 2),
    TERRIBLE("Terrible", 1)
}