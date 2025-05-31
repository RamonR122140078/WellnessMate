package com.example.wellnessmate.data.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import com.example.wellnessmate.data.entity.MoodEntryEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface MoodDao {
    @Insert
    suspend fun insert(entry: MoodEntryEntity)

    @Query("SELECT * FROM mood_table ORDER BY timestamp DESC")
    fun getAll(): Flow<List<MoodEntryEntity>>

    @Query("SELECT * FROM mood_table WHERE DATE(timestamp / 1000, 'unixepoch') == DATE('now')")
    fun getTodayMoods(): Flow<List<MoodEntryEntity>>

    @Query("SELECT * FROM mood_table WHERE moodEmoji = :emoji ORDER BY timestamp DESC")
    fun getMoodsByEmoji(emoji: String): Flow<List<MoodEntryEntity>>

    @Query("SELECT * FROM mood_table WHERE activity = :activity ORDER BY timestamp DESC")
    fun getMoodsByActivity(activity: String): Flow<List<MoodEntryEntity>>
}