package com.example.wellnessmate.data.dao

import androidx.lifecycle.LiveData
import androidx.room.*
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
}