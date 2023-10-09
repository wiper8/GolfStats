package com.example.golfstats.data.Sessions

import com.example.golfstats.data.Course.CourseRow
import kotlinx.coroutines.flow.Flow

interface SessionsRepo {
    fun getSessions(): Flow<List<SessionRow>>
    fun getCourseSessions(): Flow<List<SessionRow>>
    fun getRangeSessions(): Flow<List<SessionRow>>
    fun getItem(id: Int): Flow<SessionRow?>
    suspend fun getItemByDate(date: String): SessionRow?
    suspend fun upsert(row: SessionRow)
    suspend fun delete(row: SessionRow)
}