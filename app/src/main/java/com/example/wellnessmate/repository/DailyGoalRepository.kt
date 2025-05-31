package com.example.wellnessmate.repository

import android.content.Context
import com.example.wellnessmate.data.dao.DailyGoalDao
import com.example.wellnessmate.data.database.AppDatabase
import com.example.wellnessmate.data.entity.DailyGoalEntity
import com.example.wellnessmate.data.model.Goal
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.map

/**
 * Repository for managing goals data using Room database
 */
class GoalRepository(
    private val dailyGoalDao: DailyGoalDao
) {

    companion object {
        /**
         * Creates repository instance with database access
         */
        fun create(context: Context): GoalRepository {
            val database = AppDatabase.getInstance(context)
            return GoalRepository(database.dailyGoalDao())
        }
    }

    /**
     * Gets all goals as a Flow of UI models
     *
     * @return Flow of all goals
     */
    fun getAllGoals(): Flow<List<Goal>> {
        return dailyGoalDao.getAll().map { entities ->
            entities.map { Goal.fromEntity(it) }
        }
    }

    /**
     * Updates water goal with the given amount
     *
     * @param amount Amount of water consumed (in ml)
     */
    suspend fun updateWaterAmount(amount: Int) {
        // Fetch current goals
        val currentGoals = dailyGoalDao.getAll().first()
        val waterGoal = currentGoals.find { it.goalType == "WATER" }

        if (waterGoal != null) {
            // Update existing water goal
            dailyGoalDao.insert(DailyGoalEntity(
                goalType = "WATER",
                targetValue = waterGoal.targetValue,
                currentValue = waterGoal.currentValue + amount
            ))
        } else {
            // Create new water goal
            dailyGoalDao.insert(DailyGoalEntity(
                goalType = "WATER",
                targetValue = 2000,
                currentValue = amount
            ))
        }
    }

    /**
     * Updates the goal progress
     *
     * @param goalType Type of the goal to update
     * @param currentValue New current value
     */
    suspend fun updateGoalProgress(goalType: String, currentValue: Int) {
        // Fetch current goals
        val currentGoals = dailyGoalDao.getAll().first()
        val goal = currentGoals.find { it.goalType == goalType }

        if (goal != null) {
            dailyGoalDao.insert(DailyGoalEntity(
                goalType = goalType,
                targetValue = goal.targetValue,
                currentValue = currentValue
            ))
        }
    }

    /**
     * Adds or updates a goal
     *
     * @param goalType Type of the goal
     * @param targetValue Target value for the goal
     * @param currentValue Current progress value
     */
    suspend fun addOrUpdateGoal(goalType: String, targetValue: Int, currentValue: Int = 0) {
        val goal = DailyGoalEntity(
            goalType = goalType,
            targetValue = targetValue,
            currentValue = currentValue
        )
        dailyGoalDao.insert(goal)
    }

    /**
     * Initializes default goals if they don't exist
     */
    suspend fun initDefaultGoals() {
        val goals = dailyGoalDao.getAll().first()

        if (goals.isEmpty()) {
            // Create default goals
            val defaultGoals = listOf(
                DailyGoalEntity("WATER", 2000, 0),
                DailyGoalEntity("STEPS", 10000, 0),
                DailyGoalEntity("SLEEP", 8, 0)
            )

            defaultGoals.forEach {
                dailyGoalDao.insert(it)
            }
        }
    }
}