package com.example.wellnessmate.repository

import com.example.wellnessmate.data.dao.HydrationDao
import com.example.wellnessmate.data.entity.HydrationEntity
import kotlinx.coroutines.flow.Flow

class HydrationRepository(private val dao: HydrationDao) {
    val allEntries: Flow<List<HydrationEntity>> = dao.getAll()
    val totalToday: Flow<Int?> = dao.getTotalToday()

    suspend fun addEntry(entry: HydrationEntity) {
        dao.insert(entry)
    }
}