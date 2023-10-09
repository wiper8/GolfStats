package com.example.golfstats.data.ShotsAvailable

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Query
import androidx.room.Upsert
import kotlinx.coroutines.flow.Flow

@Dao
interface ShotsAvailableDao {
    @Upsert
    suspend fun upsert(row: ShotsAvailableRow)

    @Delete
    suspend fun delete(row: ShotsAvailableRow)

    @Query("SELECT * FROM shotsavailable_table ORDER BY id DESC")
    fun getShots(): Flow<List<ShotsAvailableRow>>

    @Query("SELECT * FROM shotsavailable_table WHERE shot = :shot LIMIT 1")
    fun getItem(shot: String): Flow<ShotsAvailableRow>
}