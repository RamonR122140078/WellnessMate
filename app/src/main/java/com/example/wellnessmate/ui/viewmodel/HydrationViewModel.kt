package com.example.wellnessmate.ui.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.wellnessmate.data.database.AppDatabase
import com.example.wellnessmate.data.entity.HydrationEntity
import com.example.wellnessmate.repository.HydrationRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.launch

class HydrationViewModel(application: Application) : AndroidViewModel(application) {
    private val repository: HydrationRepository
    val allEntries: Flow<List<HydrationEntity>>
    val totalToday: Flow<Int?>

    init {
        val dao = AppDatabase.getInstance(application).hydrationDao()
        repository = HydrationRepository(dao)
        allEntries = repository.allEntries
        totalToday = repository.totalToday
    }

    fun addWater(amountMl: Int) = viewModelScope.launch {
        val entry = HydrationEntity(amountMl = amountMl)
        repository.addEntry(entry)
    }
}