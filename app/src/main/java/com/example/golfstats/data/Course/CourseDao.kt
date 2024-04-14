package com.example.golfstats.data.Course

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Query
import androidx.room.Transaction
import androidx.room.Upsert
import kotlinx.coroutines.flow.Flow

@Dao
interface CourseDao {
    @Upsert
    suspend fun upsert(row: CourseRow)

    @Delete
    suspend fun delete(row: CourseRow)

    @Query("SELECT * FROM courses ORDER BY id DESC")
    fun getCourses(): Flow<List<CourseRow>>

    @Query("SELECT * from courses WHERE id = :id")
    fun getItem(id: Int): Flow<CourseRow>

    @Query("SELECT * from courses WHERE nom = :nom LIMIT 1")
    fun getCoursefromNom(nom: String): Flow<CourseRow>
}