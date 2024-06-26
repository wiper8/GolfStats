package com.example.golfstats.data.Holes

import com.example.golfstats.data.Shots.ShotRow
import kotlinx.coroutines.flow.Flow

interface HolesRepo {
    fun getHoles(): Flow<List<HoleRow>>
    fun getCourseHoles(course_id: Int): Flow<List<HoleRow>>
    fun getItem(id: Int): Flow<HoleRow?>
    suspend fun upsert(row: HoleRow)
    suspend fun delete(row: HoleRow)
    suspend fun deleteHolesFromCourse(id: Int)
}