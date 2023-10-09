package com.example.golfstats.data.Yardages

import kotlinx.coroutines.flow.Flow

class YardagesOffRepo(private val yardageDao: YardagesDao): YardagesRepo {
    override fun getYardages(): Flow<List<YardageRow>> = yardageDao.getYardages()
    override fun getItem(baton: String): Flow<YardageRow?> = yardageDao.getItem(baton)
    override suspend fun upsert(row: YardageRow) = yardageDao.upsert(row)
    override suspend fun delete(row: YardageRow) = yardageDao.delete(row)
}