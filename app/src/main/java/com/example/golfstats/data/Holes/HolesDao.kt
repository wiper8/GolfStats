package com.example.golfstats.data.Holes

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Query
import androidx.room.Upsert
import kotlinx.coroutines.flow.Flow

@Dao
interface HolesDao {
    @Upsert
    suspend fun upsert(row: HoleRow)

    @Delete
    suspend fun delete(row: HoleRow)

    @Query("SELECT * FROM holes ORDER BY id DESC")
    fun getHoles(): Flow<List<HoleRow>>

    @Query("SELECT * from holes WHERE id = :id")
    fun getItem(id: Int): Flow<HoleRow>
}