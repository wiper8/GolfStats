package com.example.golfstats.data.ShotsAvailable

import kotlinx.coroutines.flow.Flow

interface ShotsAvailableRepo {
    fun getShots(): Flow<List<ShotsAvailableRow>>
    fun getItem(shot: String): Flow<ShotsAvailableRow>
    suspend fun upsert(row: ShotsAvailableRow)
    suspend fun delete(row: ShotsAvailableRow)
}