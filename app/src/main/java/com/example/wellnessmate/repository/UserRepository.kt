package com.example.wellnessmate.repository

import com.example.wellnessmate.data.model.UserProfile
import com.example.wellnessmate.data.dao.UserDao
import com.example.wellnessmate.data.entity.UserEntity
import javax.inject.Inject
import javax.inject.Singleton

interface UserRepository {
    suspend fun getUserProfile(): UserProfile?
    suspend fun createUserProfile(profile: UserProfile): UserProfile
    suspend fun updateUserProfile(profile: UserProfile): UserProfile
    suspend fun clearAllData()
}

@Singleton
class UserRepositoryImpl @Inject constructor(
    private val userDao: UserDao
) : UserRepository {

    override suspend fun getUserProfile(): UserProfile? {
        return userDao.getUserProfile()?.toUserProfile()
    }

    override suspend fun createUserProfile(profile: UserProfile): UserProfile {
        val entity = profile.toUserEntity()
        val id = userDao.insertUser(entity)
        return profile.copy(id = id)
    }

    override suspend fun updateUserProfile(profile: UserProfile): UserProfile {
        val entity = profile.toUserEntity()
        userDao.updateUser(entity)
        return profile
    }

    override suspend fun clearAllData() {
        userDao.deleteAllUsers()
    }
}

// Extension functions for mapping between UserProfile and UserEntity
private fun UserProfile.toUserEntity(): UserEntity {
    return UserEntity(
        id = if (id == 0L) null else id,
        name = name,
        age = age,
        weight = weight,
        height = height,
        dailyWaterGoal = dailyWaterGoal,
        notificationsEnabled = notificationsEnabled,
        createdAt = createdAt,
        updatedAt = updatedAt
    )
}

private fun UserEntity.toUserProfile(): UserProfile {
    return UserProfile(
        id = id ?: 0L,
        name = name,
        age = age,
        weight = weight,
        height = height,
        dailyWaterGoal = dailyWaterGoal,
        notificationsEnabled = notificationsEnabled,
        createdAt = createdAt,
        updatedAt = updatedAt
    )
}