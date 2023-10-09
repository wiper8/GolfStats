package com.example.golfstats.data.Yardages

import kotlinx.coroutines.flow.Flow

interface YardagesRepo {
    fun getYardages(): Flow<List<YardageRow>>
    fun getItem(baton: String): Flow<YardageRow?>
    suspend fun upsert(row: YardageRow)
    suspend fun delete(row: YardageRow)
}