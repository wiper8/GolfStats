package com.example.golfstats.data.Holes

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Query
import androidx.room.Upsert
import com.example.golfstats.data.Shots.ShotRow
import kotlinx.coroutines.flow.Flow

@Dao
interface HolesDao {
    @Upsert
    suspend fun upsert(row: HoleRow)

    @Delete
    suspend fun delete(row: HoleRow)

    @Query("DELETE FROM holes WHERE course_id = :course_id")
    suspend fun deleteHolesFromCourse(course_id: Int)

    @Query("SELECT * FROM holes ORDER BY course_id, numero DESC")
    fun getHoles(): Flow<List<HoleRow>>

    @Query("SELECT * FROM holes WHERE course_id = :course_id ORDER BY numero")
    fun getCourseHoles(course_id: Int): Flow<List<HoleRow>>

    @Query("SELECT * from holes WHERE id = :id")
    fun getItem(id: Int): Flow<HoleRow>
}