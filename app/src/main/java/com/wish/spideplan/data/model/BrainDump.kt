package com.wish.spideplan.data.model

import androidx.room.Entity
import androidx.room.PrimaryKey
import kotlinx.datetime.LocalDateTime
import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Entity(tableName = "brain_dumps")
@Parcelize
data class BrainDump(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,
    val content: String,
    val tags: List<String> = emptyList(),
    val isArchived: Boolean = false,
    val createdAt: LocalDateTime,
    val updatedAt: LocalDateTime
) : Parcelable