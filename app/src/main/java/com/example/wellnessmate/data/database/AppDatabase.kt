package com.example.wellnessmate.data.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.example.wellnessmate.data.dao.DailyGoalDao
import com.example.wellnessmate.data.dao.HydrationDao
import com.example.wellnessmate.data.dao.MoodDao
import com.example.wellnessmate.data.dao.UserDao
import com.example.wellnessmate.data.entity.HydrationEntity
import com.example.wellnessmate.data.entity.MoodEntryEntity
import com.example.wellnessmate.data.entity.DailyGoalEntity
import com.example.wellnessmate.data.entity.UserEntity // Add this import!

@Database(
    entities = [
        HydrationEntity::class,
        MoodEntryEntity::class,
        DailyGoalEntity::class,
        UserEntity::class
    ],
    version = 2,  // Increment version since you're adding a new entity
    exportSchema = false
)
abstract class AppDatabase : RoomDatabase() {

    abstract fun hydrationDao(): HydrationDao
    abstract fun moodDao(): MoodDao
    abstract fun dailyGoalDao(): DailyGoalDao
    abstract fun userDao(): UserDao

    companion object {
        private var INSTANCE: AppDatabase? = null

        fun getInstance(context: Context): AppDatabase {
            return INSTANCE ?: synchronized(this) {
                Room.databaseBuilder(
                    context.applicationContext,
                    AppDatabase::class.java,
                    "wellnessmate.db"
                )
                    .fallbackToDestructiveMigration()
                    .build().also { INSTANCE = it }
            }
        }
    }
}