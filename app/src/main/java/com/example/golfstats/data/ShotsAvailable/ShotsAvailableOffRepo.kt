package com.example.golfstats.data.ShotsAvailable

import kotlinx.coroutines.flow.Flow


class ShotsAvailableOffRepo(private val shotavailableDao: ShotsAvailableDao): ShotsAvailableRepo {
    override fun getShots(): Flow<List<ShotsAvailableRow>> = shotavailableDao.getShots()
    override fun getItem(shot: String): Flow<ShotsAvailableRow> = shotavailableDao.getItem(shot)
    override suspend fun upsert(row: ShotsAvailableRow) = shotavailableDao.upsert(row)
    override suspend fun delete(row: ShotsAvailableRow) = shotavailableDao.delete(row)
}