package com.example.wellnessmate.data.dao

import androidx.room.*
import com.example.wellnessmate.data.entity.UserEntity

@Dao
interface UserDao {

    @Query("SELECT * FROM user_profile LIMIT 1")
    suspend fun getUserProfile(): UserEntity?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertUser(user: UserEntity): Long

    @Update
    suspend fun updateUser(user: UserEntity)

    @Delete
    suspend fun deleteUser(user: UserEntity)

    @Query("DELETE FROM user_profile")
    suspend fun deleteAllUsers()

    @Query("SELECT COUNT(*) FROM user_profile")
    suspend fun getUserCount(): Int
}