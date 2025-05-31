package com.example.wellnessmate.utils

import android.content.Context
import android.content.SharedPreferences
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class PreferencesManager @Inject constructor(
    @ApplicationContext private val context: Context
) {
    companion object {
        private const val PREF_NAME = "wellness_mate_preferences"
        private const val MODE = Context.MODE_PRIVATE
    }

    private val sharedPreferences: SharedPreferences by lazy {
        context.getSharedPreferences(PREF_NAME, MODE)
    }

    private val editor: SharedPreferences.Editor
        get() = sharedPreferences.edit()

    // String operations
    fun saveString(key: String, value: String) {
        editor.putString(key, value).apply()
    }

    fun getString(key: String, defaultValue: String = ""): String {
        return sharedPreferences.getString(key, defaultValue) ?: defaultValue
    }

    // Integer operations
    fun saveInt(key: String, value: Int) {
        editor.putInt(key, value).apply()
    }

    fun getInt(key: String, defaultValue: Int = 0): Int {
        return sharedPreferences.getInt(key, defaultValue)
    }

    // Float operations
    fun saveFloat(key: String, value: Float) {
        editor.putFloat(key, value).apply()
    }

    fun getFloat(key: String, defaultValue: Float = 0f): Float {
        return sharedPreferences.getFloat(key, defaultValue)
    }

    // Long operations
    fun saveLong(key: String, value: Long) {
        editor.putLong(key, value).apply()
    }

    fun getLong(key: String, defaultValue: Long = 0L): Long {
        return sharedPreferences.getLong(key, defaultValue)
    }

    // Boolean operations
    fun saveBoolean(key: String, value: Boolean) {
        editor.putBoolean(key, value).apply()
    }

    fun getBoolean(key: String, defaultValue: Boolean = false): Boolean {
        return sharedPreferences.getBoolean(key, defaultValue)
    }

    // Batch operations
    fun saveBatch(operations: SharedPreferences.Editor.() -> Unit) {
        editor.apply(operations).apply()
    }

    // Check if key exists
    fun contains(key: String): Boolean {
        return sharedPreferences.contains(key)
    }

    // Remove specific key
    fun remove(key: String) {
        editor.remove(key).apply()
    }

    // Clear all preferences
    fun clearAllPreferences() {
        editor.clear().apply()
    }

    // Get all preferences (for debugging)
    fun getAllPreferences(): Map<String, *> {
        return sharedPreferences.all
    }

    // Preference keys constants for type safety
    object Keys {
        const val USER_NAME = "user_name"
        const val USER_AGE = "user_age"
        const val USER_WEIGHT = "user_weight"
        const val USER_HEIGHT = "user_height"
        const val DAILY_WATER_GOAL = "daily_water_goal"
        const val NOTIFICATIONS_ENABLED = "notifications_enabled"
        const val PROFILE_UPDATED_AT = "profile_updated_at"
        const val FIRST_LAUNCH = "first_launch"
        const val ONBOARDING_COMPLETED = "onboarding_completed"
        const val THEME_MODE = "theme_mode"
        const val LANGUAGE_CODE = "language_code"
    }
}