package com.wish.spideplan.data.model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "quotes")
data class Quote(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,
    val text: String,
    val author: String,
    val source: String = "", // Movie name or context
    val category: QuoteCategory = QuoteCategory.MOTIVATION,
    val isFavorite: Boolean = false
)

enum class QuoteCategory(val displayName: String) {
    MOTIVATION("Motivation"),
    RESPONSIBILITY("Responsibility"),
    PERSEVERANCE("Perseverance"),
    COURAGE("Courage"),
    WISDOM("Wisdom")
}