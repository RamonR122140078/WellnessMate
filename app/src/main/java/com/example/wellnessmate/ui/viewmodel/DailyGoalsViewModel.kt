package com.example.wellnessmate.ui.viewmodel

import android.app.Application
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.wellnessmate.data.database.AppDatabase
import com.example.wellnessmate.repository.DailyGoalRepository
import kotlinx.coroutines.launch

class DailyGoalsViewModel(application: Application) : ViewModel() {
    private lateinit var repository: DailyGoalRepository

    val allGoals = repository.allGoals

    init {
        val dao = AppDatabase.getInstance(application).dailyGoalDao()
        repository = DailyGoalRepository(dao)
    }

    fun updateWaterGoal(value: Int) = viewModelScope.launch {
        repository.updateGoal("water", value)
    }
}