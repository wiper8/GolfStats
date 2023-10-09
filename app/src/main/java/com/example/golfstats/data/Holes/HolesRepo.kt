package com.example.golfstats.data.Holes

import kotlinx.coroutines.flow.Flow

interface HolesRepo {
    fun getHoles(): Flow<List<HoleRow>>
    fun getItem(id: Int): Flow<HoleRow?>
    suspend fun upsert(row: HoleRow)
    suspend fun delete(row: HoleRow)
}