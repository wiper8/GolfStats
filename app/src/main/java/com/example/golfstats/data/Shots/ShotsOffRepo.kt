package com.example.golfstats.data.Shots

import kotlinx.coroutines.flow.Flow

class ShotsOffRepo(private val shotDao: ShotsDao): ShotsRepo {
    override fun getShots(): Flow<List<ShotRow>> = shotDao.getShots()
    override fun getSessionShots(session_id: Int): Flow<List<ShotRow>> = shotDao.getSessionShots(session_id)
    override fun getUniqueShots(session_id: Int): Flow<List<String>> = shotDao.getUniqueShots(session_id)
    override fun getItem(id: Int): Flow<ShotRow?> = shotDao.getItem(id)
    override suspend fun upsert(row: ShotRow) = shotDao.upsert(row)
    override suspend fun delete(row: ShotRow) = shotDao.delete(row)
    //override fun getSuccess(session_id: Int, baton: String): Flow<List<Int>> = shotDao.getSuccess(session_id, baton)
    //override fun getSuccessTry(session_id: Int, baton: String): Flow<Int> = shotDao.getSuccessTry(session_id, baton)
}