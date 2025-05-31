package com.example.wellnessmate.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import com.example.wellnessmate.data.model.UserProfile
import com.example.wellnessmate.repository.UserRepository
import com.example.wellnessmate.repository.NotificationRepository
import com.example.wellnessmate.utils.PreferencesManager
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

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
)

data class SettingsUiState(
    val userProfile: UserProfile = UserProfile(),
    val isLoading: Boolean = false,
    val isSaving: Boolean = false,
    val saveSuccess: Boolean = false,
    val errorMessage: String? = null,
    val clearDataSuccess: Boolean = false
)

@HiltViewModel
class SettingsViewModel @Inject constructor(
    private val userRepository: UserRepository,
    private val notificationRepository: NotificationRepository,
    private val preferencesManager: PreferencesManager
) : ViewModel() {

    private val _uiState = MutableStateFlow(SettingsUiState())
    val uiState: StateFlow<SettingsUiState> = _uiState.asStateFlow()

    private val _userProfile = MutableStateFlow(UserProfile())
    val userProfile: StateFlow<UserProfile> = _userProfile.asStateFlow()

    init {
        loadUserProfile()
    }

    /**
     * Load user profile from repository
     */
    private fun loadUserProfile() {
        viewModelScope.launch {
            try {
                _uiState.value = _uiState.value.copy(isLoading = true, errorMessage = null)

                val profile = userRepository.getUserProfile() ?: UserProfile()
                _userProfile.value = profile
                _uiState.value = _uiState.value.copy(
                    userProfile = profile,
                    isLoading = false
                )
            } catch (e: Exception) {
                _uiState.value = _uiState.value.copy(
                    isLoading = false,
                    errorMessage = "Gagal memuat profil: ${e.message}"
                )
            }
        }
    }

    /**
     * Save user profile with all settings
     */
    fun saveUserProfile(
        name: String,
        age: Int,
        weight: Float,
        height: Float,
        dailyGoal: Int,
        notificationsEnabled: Boolean
    ) {
        viewModelScope.launch {
            try {
                _uiState.value = _uiState.value.copy(isSaving = true, errorMessage = null)

                // Validate input data
                val validationResult = validateUserInput(name, age, weight, height, dailyGoal)
                if (!validationResult.isValid) {
                    _uiState.value = _uiState.value.copy(
                        isSaving = false,
                        errorMessage = validationResult.errorMessage
                    )
                    return@launch
                }

                // Create updated profile
                val updatedProfile = _userProfile.value.copy(
                    name = name.trim(),
                    age = age,
                    weight = weight,
                    height = height,
                    dailyWaterGoal = dailyGoal,
                    notificationsEnabled = notificationsEnabled,
                    updatedAt = System.currentTimeMillis()
                )

                // Save to repository
                val savedProfile = if (updatedProfile.id == 0L) {
                    userRepository.createUserProfile(updatedProfile)
                } else {
                    userRepository.updateUserProfile(updatedProfile)
                }

                // Update notification settings
                updateNotificationSettings(notificationsEnabled)

                // Save preferences
                savePreferences(savedProfile)

                _userProfile.value = savedProfile
                _uiState.value = _uiState.value.copy(
                    userProfile = savedProfile,
                    isSaving = false,
                    saveSuccess = true
                )

                // Reset success flag after a delay
                kotlinx.coroutines.delay(3000)
                _uiState.value = _uiState.value.copy(saveSuccess = false)

            } catch (e: Exception) {
                _uiState.value = _uiState.value.copy(
                    isSaving = false,
                    errorMessage = "Gagal menyimpan profil: ${e.message}"
                )
            }
        }
    }

    /**
     * Update water goal only
     */
    fun updateWaterGoal(newGoal: Int) {
        viewModelScope.launch {
            try {
                val currentProfile = _userProfile.value
                val updatedProfile = currentProfile.copy(
                    dailyWaterGoal = newGoal,
                    updatedAt = System.currentTimeMillis()
                )

                userRepository.updateUserProfile(updatedProfile)
                _userProfile.value = updatedProfile
                _uiState.value = _uiState.value.copy(userProfile = updatedProfile)

            } catch (e: Exception) {
                _uiState.value = _uiState.value.copy(
                    errorMessage = "Gagal memperbarui target air: ${e.message}"
                )
            }
        }
    }

    /**
     * Toggle notification settings
     */
    fun toggleNotifications(enabled: Boolean) {
        viewModelScope.launch {
            try {
                updateNotificationSettings(enabled)

                val updatedProfile = _userProfile.value.copy(
                    notificationsEnabled = enabled,
                    updatedAt = System.currentTimeMillis()
                )

                userRepository.updateUserProfile(updatedProfile)
                _userProfile.value = updatedProfile
                _uiState.value = _uiState.value.copy(userProfile = updatedProfile)

            } catch (e: Exception) {
                _uiState.value = _uiState.value.copy(
                    errorMessage = "Gagal mengatur notifikasi: ${e.message}"
                )
            }
        }
    }

    /**
     * Clear all app data
     */
    fun clearAllData() {
        viewModelScope.launch {
            try {
                _uiState.value = _uiState.value.copy(isLoading = true, errorMessage = null)

                // Clear all repositories
                userRepository.clearAllData()
                notificationRepository.cancelAllNotifications()
                preferencesManager.clearAllPreferences()

                // Reset UI state
                val defaultProfile = UserProfile()
                _userProfile.value = defaultProfile
                _uiState.value = SettingsUiState(
                    userProfile = defaultProfile,
                    clearDataSuccess = true
                )

                // Reset success flag
                kotlinx.coroutines.delay(3000)
                _uiState.value = _uiState.value.copy(clearDataSuccess = false)

            } catch (e: Exception) {
                _uiState.value = _uiState.value.copy(
                    isLoading = false,
                    errorMessage = "Gagal menghapus data: ${e.message}"
                )
            }
        }
    }

    /**
     * Calculate recommended daily water intake
     */
    fun calculateRecommendedWaterIntake(weight: Float?, age: Int?): Int {
        if (weight == null || weight <= 0) return 2000

        // Base calculation: 35ml per kg of body weight
        var baseIntake = (weight * 35).toInt()

        // Age-based adjustments
        age?.let {
            baseIntake = when {
                it < 18 -> (baseIntake * 0.9).toInt() // Youth: 10% less
                it in 18..30 -> baseIntake // Young adults: baseline
                it in 31..50 -> (baseIntake * 1.05).toInt() // Middle-aged: 5% more
                it in 51..65 -> (baseIntake * 1.1).toInt() // Mature adults: 10% more
                else -> (baseIntake * 1.15).toInt() // Elderly: 15% more
            }
        }

        // Activity level adjustment (can be enhanced later)
        // For now, assume moderate activity level

        // Climate adjustment (can be enhanced with location data)
        // For now, use standard calculation

        // Ensure reasonable range
        return baseIntake.coerceIn(1500, 4000)
    }

    /**
     * Get BMI calculation
     */
    fun calculateBMI(weight: Float, height: Float): Float? {
        return if (weight > 0 && height > 0) {
            val heightInMeters = height / 100
            weight / (heightInMeters * heightInMeters)
        } else null
    }

    /**
     * Get BMI category
     */
    fun getBMICategory(bmi: Float): String {
        return when {
            bmi < 18.5 -> "Underweight"
            bmi < 25.0 -> "Normal"
            bmi < 30.0 -> "Overweight"
            else -> "Obese"
        }
    }

    /**
     * Clear error message
     */
    fun clearError() {
        _uiState.value = _uiState.value.copy(errorMessage = null)
    }

    /**
     * Reset save success state
     */
    fun resetSaveSuccess() {
        _uiState.value = _uiState.value.copy(saveSuccess = false)
    }

    // Private helper methods

    private suspend fun updateNotificationSettings(enabled: Boolean) {
        if (enabled) {
            // Schedule hydration reminders
            notificationRepository.scheduleHydrationReminders(
                startHour = 7,
                endHour = 21,
                intervalHours = 2
            )
        } else {
            // Cancel all hydration reminders
            notificationRepository.cancelHydrationReminders()
        }
    }

    private suspend fun savePreferences(profile: UserProfile) {
        preferencesManager.apply {
            saveString("user_name", profile.name)
            saveInt("user_age", profile.age)
            saveFloat("user_weight", profile.weight)
            saveFloat("user_height", profile.height)
            saveInt("daily_water_goal", profile.dailyWaterGoal)
            saveBoolean("notifications_enabled", profile.notificationsEnabled)
            saveLong("profile_updated_at", profile.updatedAt)
        }
    }

    private data class ValidationResult(
        val isValid: Boolean,
        val errorMessage: String? = null
    )

    private fun validateUserInput(
        name: String,
        age: Int,
        weight: Float,
        height: Float,
        dailyGoal: Int
    ): ValidationResult {
        return when {
            name.trim().isEmpty() -> ValidationResult(false, "Nama tidak boleh kosong")
            name.trim().length < 2 -> ValidationResult(false, "Nama minimal 2 karakter")
            age < 1 || age > 120 -> ValidationResult(false, "Usia harus antara 1-120 tahun")
            weight < 10 || weight > 300 -> ValidationResult(false, "Berat badan harus antara 10-300 kg")
            height < 50 || height > 250 -> ValidationResult(false, "Tinggi badan harus antara 50-250 cm")
            dailyGoal < 500 || dailyGoal > 5000 -> ValidationResult(false, "Target air harus antara 500-5000 ml")
            else -> ValidationResult(true)
        }
    }
}

// Extension functions for easier state access
fun SettingsViewModel.getCurrentProfile(): UserProfile {
    return userProfile.value
}

fun SettingsViewModel.isProfileComplete(): Boolean {
    val profile = getCurrentProfile()
    return profile.name.isNotEmpty() &&
            profile.age > 0 &&
            profile.weight > 0 &&
            profile.height > 0
}

fun SettingsViewModel.getProfileCompletionPercentage(): Int {
    val profile = getCurrentProfile()
    var completedFields = 0
    val totalFields = 5

    if (profile.name.isNotEmpty()) completedFields++
    if (profile.age > 0) completedFields++
    if (profile.weight > 0) completedFields++
    if (profile.height > 0) completedFields++
    if (profile.dailyWaterGoal > 0) completedFields++

    return (completedFields * 100) / totalFields
}