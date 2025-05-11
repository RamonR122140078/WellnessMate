package com.example.wellnessmate.data.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "daily_goal_table")
data class DailyGoalEntity(
    @PrimaryKey val goalType: String,
    val targetValue: Int,
    val currentValue: Int = 0
)