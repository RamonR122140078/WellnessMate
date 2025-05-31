package com.example.wellnessmate.data.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "user_profile")
data class UserEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Long? = null,
    val name: String,
    val age: Int,
    val weight: Float,
    val height: Float,
    val dailyWaterGoal: Int,
    val notificationsEnabled: Boolean,
    val createdAt: Long,
    val updatedAt: Long
)