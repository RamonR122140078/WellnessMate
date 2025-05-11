package com.example.wellnessmate.data.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.example.wellnessmate.data.dao.DailyGoalDao
import com.example.wellnessmate.data.dao.HydrationDao
import com.example.wellnessmate.data.dao.MoodDao // Pastikan import ini ada
import com.example.wellnessmate.data.entity.HydrationEntity
import com.example.wellnessmate.data.entity.MoodEntryEntity // Sesuaikan jika kamu punya entitas ini
import com.example.wellnessmate.data.entity.DailyGoalEntity

@Database(
    entities = [HydrationEntity::class, MoodEntryEntity::class, DailyGoalEntity::class], // Tambahkan entitas Mood jika sudah ada
    version = 1,
    exportSchema = false
)
abstract class AppDatabase : RoomDatabase() {

    abstract fun hydrationDao(): HydrationDao
    abstract fun moodDao(): MoodDao // Harus mengembalikan interface MoodDao
    abstract fun dailyGoalDao(): DailyGoalDao
    companion object {
        private var INSTANCE: AppDatabase? = null

        fun getInstance(context: Context): AppDatabase {
            return INSTANCE ?: synchronized(this) {
                Room.databaseBuilder(
                    context.applicationContext,
                    AppDatabase::class.java,
                    "wellnessmate.db"
                ).build().also { INSTANCE = it }
            }
        }
    }
}