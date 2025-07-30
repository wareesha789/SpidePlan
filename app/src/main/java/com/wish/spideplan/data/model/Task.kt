package com.wish.spideplan.data.model

import androidx.room.Entity
import androidx.room.PrimaryKey
import kotlinx.datetime.LocalDateTime
import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Entity(tableName = "tasks")
@Parcelize
data class Task(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,
    val title: String,
    val description: String = "",
    val category: TaskCategory,
    val priority: TaskPriority = TaskPriority.MEDIUM,
    val scheduledDateTime: LocalDateTime? = null,
    val isCompleted: Boolean = false,
    val isRecurring: Boolean = false,
    val recurringType: RecurringType? = null,
    val reminderMinutesBefore: Int? = null,
    val createdAt: LocalDateTime,
    val updatedAt: LocalDateTime,
    val completedAt: LocalDateTime? = null
) : Parcelable

enum class TaskCategory(val displayName: String, val color: String) {
    WORK("Work", "#FF5722"),
    PERSONAL("Personal", "#2196F3"),
    HEALTH("Health", "#4CAF50"),
    LEARNING("Learning", "#FF9800"),
    SOCIAL("Social", "#9C27B0"),
    CHORES("Chores", "#795548"),
    OTHER("Other", "#607D8B")
}

enum class TaskPriority(val displayName: String, val value: Int) {
    HIGH("High", 3),
    MEDIUM("Medium", 2),
    LOW("Low", 1)
}

enum class RecurringType(val displayName: String) {
    DAILY("Daily"),
    WEEKLY("Weekly"),
    MONTHLY("Monthly")
}