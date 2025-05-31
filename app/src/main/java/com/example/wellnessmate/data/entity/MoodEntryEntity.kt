package com.example.wellnessmate.data.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "mood_table")
data class MoodEntryEntity(
    @PrimaryKey(autoGenerate = true) val id: Int,
    val moodEmoji: String,
    val note: String?,
    val activity: String = "",  // Add activity field with default empty string
    val timestamp: Long = System.currentTimeMillis()
)

// This class is used to present mood entries with activity information in the UI
data class MoodEntryWithActivity(
    val id: Int,
    val moodEmoji: String,
    val note: String?,
    val activity: String,
    val timestamp: Long
)

// Extension function to convert MoodEntryEntity to MoodEntryWithActivity
fun MoodEntryEntity.toMoodEntryWithActivity() = MoodEntryWithActivity(
    id = id,
    moodEmoji = moodEmoji,
    note = note,
    activity = activity,
    timestamp = timestamp
)