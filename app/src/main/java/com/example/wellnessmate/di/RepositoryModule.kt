package com.example.wellnessmate.di

import com.example.wellnessmate.repository.NotificationRepository
import com.example.wellnessmate.repository.NotificationRepositoryImpl
import com.example.wellnessmate.repository.UserRepository
import com.example.wellnessmate.repository.UserRepositoryImpl
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
abstract class RepositoryModule {

    @Binds
    @Singleton
    abstract fun bindUserRepository(
        userRepositoryImpl: UserRepositoryImpl
    ): UserRepository

    @Binds
    @Singleton
    abstract fun bindNotificationRepository(
        notificationRepositoryImpl: NotificationRepositoryImpl
    ): NotificationRepository
}