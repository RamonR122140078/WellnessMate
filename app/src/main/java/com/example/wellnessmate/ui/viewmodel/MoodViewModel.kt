package com.example.wellnessmate.ui.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.wellnessmate.data.database.AppDatabase
import com.example.wellnessmate.data.entity.MoodEntryEntity
import com.example.wellnessmate.data.entity.MoodEntryWithActivity
import com.example.wellnessmate.repository.MoodRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.launch

class MoodViewModel(application: Application) : AndroidViewModel(application) {
    private val repository: MoodRepository
    val allMoods: Flow<List<MoodEntryWithActivity>>

    init {
        val dao = AppDatabase.getInstance(application).moodDao()
        repository = MoodRepository(dao)
        allMoods = repository.allMoods
    }

    fun addMood(emoji: String, note: String?, activity: String) = viewModelScope.launch {
        repository.addMood(
            MoodEntryEntity(
                id = 0,
                moodEmoji = emoji,
                note = note,
                activity = activity,
                timestamp = System.currentTimeMillis()
            )
        )
    }

    fun getMoodsByActivity(activity: String) = viewModelScope.launch {
        repository.getMoodsByActivity(activity)
    }

    fun getTodayMoods() = repository.getTodayMoods()
}