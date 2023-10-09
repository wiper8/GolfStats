package com.example.golfstats.data.Course

import kotlinx.coroutines.flow.Flow

interface CourseRepo {
    fun getCourses(): Flow<List<CourseRow>>
    fun getItem(id: Int): Flow<CourseRow?>
    fun getCoursefromNom(nom: String): Flow<CourseRow?>
    suspend fun upsert(row: CourseRow)
    suspend fun delete(row: CourseRow)
}