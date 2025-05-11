package com.example.wellnessmate.ui.viewmodel

import android.app.Application
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.wellnessmate.data.database.AppDatabase
import com.example.wellnessmate.data.entity.MoodEntryEntity
import com.example.wellnessmate.repository.MoodRepository
import kotlinx.coroutines.launch

class MoodViewModel(application: Application) : ViewModel() {
    private lateinit var repository: MoodRepository

    val allMoods = repository.allMoods

    init {
        val dao = AppDatabase.getInstance(application).moodDao()
        repository = MoodRepository(dao)
    }

    fun addMood(emoji: String, note: String?) = viewModelScope.launch {
        repository.addMood(MoodEntryEntity(id = 0, moodEmoji = emoji, note = note))
    }
}