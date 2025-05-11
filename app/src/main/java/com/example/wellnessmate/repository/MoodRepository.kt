package com.example.wellnessmate.repository

import com.example.wellnessmate.data.dao.MoodDao
import com.example.wellnessmate.data.entity.MoodEntryEntity
import kotlinx.coroutines.flow.Flow

class MoodRepository(private val dao: MoodDao) {
    val allMoods: Flow<List<MoodEntryEntity>> = dao.getAll()

    suspend fun addMood(entry: MoodEntryEntity) {
        dao.insert(entry)
    }
}