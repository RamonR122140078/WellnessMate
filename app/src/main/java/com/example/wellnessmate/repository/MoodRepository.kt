package com.example.wellnessmate.repository

import com.example.wellnessmate.data.dao.MoodDao
import com.example.wellnessmate.data.entity.MoodEntryEntity
import com.example.wellnessmate.data.entity.MoodEntryWithActivity
import com.example.wellnessmate.data.entity.toMoodEntryWithActivity
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class MoodRepository(private val dao: MoodDao) {
    // Transform Flow<List<MoodEntryEntity>> to Flow<List<MoodEntryWithActivity>>
    val allMoods: Flow<List<MoodEntryWithActivity>> = dao.getAll().map { list ->
        list.map { it.toMoodEntryWithActivity() }
    }

    suspend fun addMood(entry: MoodEntryEntity) {
        dao.insert(entry)
    }

    fun getTodayMoods(): Flow<List<MoodEntryWithActivity>> = dao.getTodayMoods().map { list ->
        list.map { it.toMoodEntryWithActivity() }
    }

    fun getMoodsByEmoji(emoji: String): Flow<List<MoodEntryWithActivity>> =
        dao.getMoodsByEmoji(emoji).map { list ->
            list.map { it.toMoodEntryWithActivity() }
        }

    fun getMoodsByActivity(activity: String): Flow<List<MoodEntryWithActivity>> =
        dao.getMoodsByActivity(activity).map { list ->
            list.map { it.toMoodEntryWithActivity() }
        }
}