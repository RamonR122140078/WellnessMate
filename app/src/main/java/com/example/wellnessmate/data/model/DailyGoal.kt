package com.example.wellnessmate.data.model

import com.example.wellnessmate.data.entity.DailyGoalEntity

/**
 * Represents a daily wellness goal (UI model)
 *
 * @property goalType Type of goal (WATER, STEPS, SLEEP, etc)
 * @property name Display name of the goal
 * @property description Brief description of what the goal tracks
 * @property currentValue Current progress toward the goal
 * @property targetValue Target value to reach
 * @property unit Unit of measurement (ml, steps, hours, etc)
 */
data class Goal(
    val goalType: String,
    val name: String,
    val description: String,
    val currentValue: Int,
    val targetValue: Int,
    val unit: String
) {
    companion object {
        /**
         * Maps a DailyGoalEntity to a UI Goal model
         */
        fun fromEntity(entity: DailyGoalEntity): Goal {
            val metadata = getGoalMetadata(entity.goalType)
            return Goal(
                goalType = entity.goalType,
                name = metadata.name,
                description = metadata.description,
                currentValue = entity.currentValue,
                targetValue = entity.targetValue,
                unit = metadata.unit
            )
        }

        /**
         * Provides display metadata for different goal types
         */
        private fun getGoalMetadata(goalType: String): GoalMetadata {
            return when (goalType) {
                "WATER" -> GoalMetadata("Air Minum", "Konsumsi air harian", "ml")
                "STEPS" -> GoalMetadata("Langkah", "Jumlah langkah harian", "langkah")
                "SLEEP" -> GoalMetadata("Tidur", "Durasi tidur", "jam")
                "MEDITATION" -> GoalMetadata("Meditasi", "Waktu meditasi", "menit")
                "EXERCISE" -> GoalMetadata("Olahraga", "Durasi aktivitas fisik", "menit")
                else -> GoalMetadata(goalType, "Target harian", "unit")
            }
        }
    }
}

/**
 * Helper class for goal display information
 */
private data class GoalMetadata(
    val name: String,
    val description: String,
    val unit: String
)