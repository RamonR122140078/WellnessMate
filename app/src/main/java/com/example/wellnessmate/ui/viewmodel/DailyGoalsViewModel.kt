package com.example.wellnessmate.ui.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.wellnessmate.data.model.Goal
import com.example.wellnessmate.repository.GoalRepository
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

/**
 * ViewModel for managing daily wellness goals
 */
class DailyGoalsViewModel(application: Application) : AndroidViewModel(application) {

    private val repository: GoalRepository = GoalRepository.create(application)

    /**
     * Flow of all goals from the database
     */
    val allGoals: StateFlow<List<Goal>> = repository.getAllGoals()
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = emptyList()
        )

    init {
        // Initialize default goals if needed
        viewModelScope.launch {
            repository.initDefaultGoals()
        }
    }

    /**
     * Updates the water intake goal with the specified amount
     *
     * @param amount Amount of water in ml
     */
    fun updateWaterGoal(amount: Int) {
        if (amount <= 0) return

        viewModelScope.launch {
            repository.updateWaterAmount(amount)
        }
    }

    /**
     * Adds or updates a goal
     *
     * @param goalType Type of the goal
     * @param targetValue Target value for the goal
     * @param currentValue Current progress (default 0)
     */
    fun addOrUpdateGoal(goalType: String, targetValue: Int, currentValue: Int = 0) {
        viewModelScope.launch {
            repository.addOrUpdateGoal(goalType, targetValue, currentValue)
        }
    }

    /**
     * Updates the progress of a specific goal
     *
     * @param goalType Type of the goal to update
     * @param newValue New progress value
     */
    fun updateGoalProgress(goalType: String, newValue: Int) {
        viewModelScope.launch {
            repository.updateGoalProgress(goalType, newValue)
        }
    }

    /**
     * Resets all goals to zero
     */
    fun resetDailyGoals() {
        viewModelScope.launch {
            allGoals.value.forEach { goal ->
                repository.addOrUpdateGoal(goal.goalType, goal.targetValue, 0)
            }
        }
    }
}