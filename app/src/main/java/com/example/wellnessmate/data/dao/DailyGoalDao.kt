package com.example.wellnessmate.data.dao

import androidx.room.*
import com.example.wellnessmate.data.entity.DailyGoalEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface DailyGoalDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(goal: DailyGoalEntity)

    @Query("SELECT * FROM daily_goal_table")
    fun getAll(): Flow<List<DailyGoalEntity>>
}