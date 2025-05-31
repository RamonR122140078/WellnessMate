package com.example.wellnessmate.data.model

data class UserProfile(
    val id: Long = 0,
    val name: String = "",
    val age: Int = 0,
    val weight: Float = 0f,
    val height: Float = 0f,
    val dailyWaterGoal: Int = 2000,
    val notificationsEnabled: Boolean = true,
    val createdAt: Long = System.currentTimeMillis(),
    val updatedAt: Long = System.currentTimeMillis()
) {

    /**
     * Check if the profile has all required basic information
     */
    fun isComplete(): Boolean {
        return name.isNotEmpty() &&
                age > 0 &&
                weight > 0 &&
                height > 0
    }

    /**
     * Calculate completion percentage of the profile
     */
    fun getCompletionPercentage(): Int {
        var completedFields = 0
        val totalFields = 5

        if (name.isNotEmpty()) completedFields++
        if (age > 0) completedFields++
        if (weight > 0) completedFields++
        if (height > 0) completedFields++
        if (dailyWaterGoal > 0) completedFields++

        return (completedFields * 100) / totalFields
    }

    /**
     * Calculate BMI (Body Mass Index)
     */
    fun calculateBMI(): Float? {
        return if (weight > 0 && height > 0) {
            val heightInMeters = height / 100
            weight / (heightInMeters * heightInMeters)
        } else null
    }

    /**
     * Get BMI category based on WHO standards
     */
    fun getBMICategory(): String? {
        val bmi = calculateBMI() ?: return null
        return when {
            bmi < 18.5 -> "Underweight"
            bmi < 25.0 -> "Normal"
            bmi < 30.0 -> "Overweight"
            else -> "Obese"
        }
    }

    /**
     * Calculate recommended daily water intake based on weight and age
     */
    fun calculateRecommendedWaterIntake(): Int {
        if (weight <= 0) return 2000

        // Base calculation: 35ml per kg of body weight
        var baseIntake = (weight * 35).toInt()

        // Age-based adjustments
        if (age > 0) {
            baseIntake = when {
                age < 18 -> (baseIntake * 0.9).toInt() // Youth: 10% less
                age in 18..30 -> baseIntake // Young adults: baseline
                age in 31..50 -> (baseIntake * 1.05).toInt() // Middle-aged: 5% more
                age in 51..65 -> (baseIntake * 1.1).toInt() // Mature adults: 10% more
                else -> (baseIntake * 1.15).toInt() // Elderly: 15% more
            }
        }

        // Ensure reasonable range
        return baseIntake.coerceIn(1500, 4000)
    }

    /**
     * Get display name for UI
     */
    fun getDisplayName(): String {
        return name.ifEmpty { "User" }
    }

    /**
     * Check if profile was recently updated (within last 24 hours)
     */
    fun isRecentlyUpdated(): Boolean {
        val twentyFourHoursAgo = System.currentTimeMillis() - (24 * 60 * 60 * 1000)
        return updatedAt > twentyFourHoursAgo
    }

    /**
     * Format height for display (e.g., "175 cm")
     */
    fun getFormattedHeight(): String {
        return if (height > 0) "${height.toInt()} cm" else "Not set"
    }

    /**
     * Format weight for display (e.g., "70.5 kg")
     */
    fun getFormattedWeight(): String {
        return if (weight > 0) "$weight kg" else "Not set"
    }

    /**
     * Format age for display (e.g., "25 years")
     */
    fun getFormattedAge(): String {
        return if (age > 0) "$age years" else "Not set"
    }

    /**
     * Format daily water goal for display (e.g., "2.0 L")
     */
    fun getFormattedWaterGoal(): String {
        return "${dailyWaterGoal / 1000.0} L"
    }
}