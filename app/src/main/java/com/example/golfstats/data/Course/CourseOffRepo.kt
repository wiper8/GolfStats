package com.example.golfstats.data.Course

import kotlinx.coroutines.flow.Flow

class CourseOffRepo(private val dao: CourseDao): CourseRepo {
    override fun getCourses(): Flow<List<CourseRow>> = dao.getCourses()
    override fun getItem(id: Int): Flow<CourseRow?> = dao.getItem(id)
    override fun getCoursefromNom(nom: String): Flow<CourseRow?> = dao.getCoursefromNom(nom)
    override suspend fun upsert(row: CourseRow) = dao.upsert(row)
    override suspend fun delete(row: CourseRow) = dao.delete(row)
}