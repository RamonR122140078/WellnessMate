package com.example.wellnessmate.repository

import com.example.wellnessmate.data.dao.DailyGoalDao
import com.example.wellnessmate.data.entity.DailyGoalEntity
import kotlinx.coroutines.flow.Flow

class DailyGoalRepository(private val dao: DailyGoalDao) {
    val allGoals: Flow<List<DailyGoalEntity>> = dao.getAll()

    suspend fun updateGoal(goalType: String, newValue: Int) {
        dao.insert(DailyGoalEntity(goalType, 2000, newValue))
    }
}