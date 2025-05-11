package com.example.wellnessmate.di

import android.app.Application
import androidx.lifecycle.ViewModelProvider
import com.example.wellnessmate.data.database.AppDatabase
import com.example.wellnessmate.repository.HydrationRepository

object Injector {
    fun provideHydrationViewModelFactory(application: Application): ViewModelProvider.Factory {
        val dao = AppDatabase.getInstance(application).hydrationDao()
        val repository = HydrationRepository(dao)
        return HydrationViewModelFactory(repository, application)
    }
}