package com.example.wellnessmate.data.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "mood_table")
data class MoodEntryEntity(
    @PrimaryKey(autoGenerate = true) val id: Int,
    val moodEmoji: String,
    val note: String?,
    val timestamp: Long = System.currentTimeMillis()
)