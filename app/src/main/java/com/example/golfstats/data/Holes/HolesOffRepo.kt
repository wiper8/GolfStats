package com.example.golfstats.data.Holes

import com.example.golfstats.data.Shots.ShotRow
import kotlinx.coroutines.flow.Flow

class HolesOffRepo(private val dao: HolesDao): HolesRepo {
    override fun getHoles(): Flow<List<HoleRow>> = dao.getHoles()
    override fun getCourseHoles(course_id: Int): Flow<List<HoleRow>> = dao.getCourseHoles(course_id)
    override fun getItem(id: Int): Flow<HoleRow?> = dao.getItem(id)
    override suspend fun upsert(row: HoleRow) = dao.upsert(row)
    override suspend fun delete(row: HoleRow) = dao.delete(row)
    override suspend fun deleteHolesFromCourse(id: Int) = dao.deleteHolesFromCourse(id)
}