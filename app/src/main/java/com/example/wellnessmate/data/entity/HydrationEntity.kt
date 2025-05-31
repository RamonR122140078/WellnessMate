package com.example.wellnessmate.data.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

@Entity(tableName = "hydration_table")
data class HydrationEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,

    @ColumnInfo(name = "amount_ml")
    val amountMl: Int,

    @ColumnInfo(name = "timestamp")
    val timestamp: Long = System.currentTimeMillis(),

    @ColumnInfo(name = "drink_type")
    val drinkType: String = "water", // water, tea, coffee, juice, etc.

    @ColumnInfo(name = "container_size")
    val containerSize: Int? = null, // 250ml, 500ml, 1000ml for quick tracking

    @ColumnInfo(name = "note")
    val note: String? = null, // Optional note from user

    @ColumnInfo(name = "date_only")
    val dateOnly: String = getCurrentDateString(), // For easier daily grouping (YYYY-MM-DD format)

    @ColumnInfo(name = "is_goal_contributing")
    val isGoalContributing: Boolean = true // Some drinks might not count toward daily goal
) {
    companion object {
        private fun getCurrentDateString(): String {
            val sdf = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
            return sdf.format(Date())
        }
    }

    // Utility functions for UI
    fun getFormattedTime(): String {
        val sdf = SimpleDateFormat("HH:mm", Locale.getDefault())
        return sdf.format(Date(timestamp))
    }

    fun getFormattedDateTime(): String {
        val sdf = SimpleDateFormat("dd/MM/yyyy HH:mm", Locale.getDefault())
        return sdf.format(Date(timestamp))
    }

    fun getDisplayText(): String {
        return when {
            containerSize != null -> "$amountMl ml (${getDrinkTypeEmoji()} ${containerSize}ml)"
            else -> "$amountMl ml ${getDrinkTypeEmoji()}"
        }
    }

    private fun getDrinkTypeEmoji(): String {
        return when (drinkType.lowercase()) {
            "water" -> "💧"
            "tea" -> "🍵"
            "coffee" -> "☕"
            "juice" -> "🧃"
            "sports_drink" -> "🥤"
            "milk" -> "🥛"
            else -> "💧"
        }
    }

    fun isToday(): Boolean {
        val today = getCurrentDateString()
        return dateOnly == today
    }

    fun isThisWeek(): Boolean {
        val calendar = java.util.Calendar.getInstance()
        val entryCalendar = java.util.Calendar.getInstance().apply {
            timeInMillis = timestamp
        }

        val thisWeek = calendar.get(java.util.Calendar.WEEK_OF_YEAR)
        val thisYear = calendar.get(java.util.Calendar.YEAR)

        val entryWeek = entryCalendar.get(java.util.Calendar.WEEK_OF_YEAR)
        val entryYear = entryCalendar.get(java.util.Calendar.YEAR)

        return thisWeek == entryWeek && thisYear == entryYear
    }

    fun isThisMonth(): Boolean {
        val calendar = java.util.Calendar.getInstance()
        val entryCalendar = java.util.Calendar.getInstance().apply {
            timeInMillis = timestamp
        }

        val thisMonth = calendar.get(java.util.Calendar.MONTH)
        val thisYear = calendar.get(java.util.Calendar.YEAR)

        val entryMonth = entryCalendar.get(java.util.Calendar.MONTH)
        val entryYear = entryCalendar.get(java.util.Calendar.YEAR)

        return thisMonth == entryMonth && thisYear == entryYear
    }
}