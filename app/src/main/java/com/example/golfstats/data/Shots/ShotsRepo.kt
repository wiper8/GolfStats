package com.example.golfstats.data.Shots

import kotlinx.coroutines.flow.Flow

interface ShotsRepo {
    fun getShots(): Flow<List<ShotRow>>
    fun getSessionShots(session_id: Int): Flow<List<ShotRow>>
    fun getUniqueAllShots(): Flow<List<String>>
    fun getUniqueShots(session_id: Int): Flow<List<String>>
    fun getItem(id: Int): Flow<ShotRow?>
    suspend fun upsert(row: ShotRow)
    suspend fun delete(row: ShotRow)
}