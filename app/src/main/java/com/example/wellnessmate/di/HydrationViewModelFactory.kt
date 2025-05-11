package com.example.wellnessmate.di

import android.app.Application
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.wellnessmate.repository.HydrationRepository
import com.example.wellnessmate.ui.viewmodel.HydrationViewModel

class HydrationViewModelFactory(
    private val repository: HydrationRepository,
    private val application: Application
) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(HydrationViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return HydrationViewModel(application) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}