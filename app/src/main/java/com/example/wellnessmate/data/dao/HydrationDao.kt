package com.example.wellnessmate.data.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.wellnessmate.data.entity.HydrationEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface HydrationDao {
    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insert(entry: HydrationEntity)

    @Query("SELECT * FROM hydration_table ORDER BY timestamp DESC")
    fun getAll(): Flow<List<HydrationEntity>>

    @Query("SELECT SUM(amount_ml) FROM hydration_table WHERE DATE(timestamp / 1000, 'unixepoch') == DATE('now')")
    fun getTotalToday(): Flow<Int?>
}