package com.example.golfstats.data.Shots

import kotlinx.coroutines.flow.Flow

interface ShotsRepo {
    fun getShots(): Flow<List<ShotRow>>
    fun getSessionShots(session_id: Int): Flow<List<ShotRow>>
    fun getUniqueShots(session_id: Int): Flow<List<String>>
    fun getItem(id: Int): Flow<ShotRow?>
    suspend fun upsert(row: ShotRow)
    suspend fun delete(row: ShotRow)
    fun getSuccess(session_id: Int, baton: String): Flow<Int>
    fun getSuccessTry(session_id: Int, baton: String): Flow<Int>
    fun getGreen(session_id: Int): Flow<Int>
    fun getGreenTry(session_id: Int): Flow<Int>
    fun getPenalty(session_id: Int): Flow<Int>
    fun getPenaltyTry(session_id: Int): Flow<Int>
    fun getReset(session_id: Int): Flow<Int>
    fun getResetTry(session_id: Int): Flow<Int>

}