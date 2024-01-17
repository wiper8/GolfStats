package com.example.golfstats.data.Sessions

import com.example.golfstats.data.Course.CourseRow
import kotlinx.coroutines.flow.Flow

class SessionsOffRepo(private val dao: SessionsDao): SessionsRepo {
    override fun getSessions(): Flow<List<SessionRow>> = dao.getSessions()
    override fun getSessionIdFromDate(date: String): Flow<SessionRow> = dao.getSessionIdFromDate(date)
    override fun getCourseSessions(): Flow<List<SessionRow>> = dao.getCourseSessions()
    override fun getRangeSessions(): Flow<List<SessionRow>> = dao.getRangeSessions()
    override fun getItem(id: Int): Flow<SessionRow?> = dao.getItem(id)
    override suspend fun getItemByDate(date: String): SessionRow? = dao.getItemByDate(date)
    override suspend fun upsert(row: SessionRow) = dao.upsert(row)
    override suspend fun delete(row: SessionRow) = dao.delete(row)
}