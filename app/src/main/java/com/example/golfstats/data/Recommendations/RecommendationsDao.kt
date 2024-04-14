package com.example.golfstats.data.Recommendations

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Query
import androidx.room.Upsert
import kotlinx.coroutines.flow.Flow


@Dao
interface RecommendationsDao {
    @Upsert
    suspend fun upsert(row: RecommendationRow)

    @Delete
    suspend fun delete(row: RecommendationRow)

    @Query("SELECT * FROM recommendations_table ORDER BY num") //TODO DESC ou pas?
    fun getRecommendations(): Flow<List<RecommendationRow>>

    @Query("SELECT * FROM recommendations_table WHERE hole_id = :hole_id AND recommend_id = :recommend_id ORDER BY num")
    fun getRecommendationsHole(hole_id: Int, recommend_id: Int): Flow<List<RecommendationRow>>

    @Query("SELECT recommend_id FROM (SELECT DISTINCT hole_id, recommend_id FROM recommendations_table WHERE hole_id = :hole_id)")
    fun getUniqueRecommendationsHole(hole_id: Int): Flow<List<Int>>

    @Query("SELECT DISTINCT recommend_id FROM recommendations_table")
    fun getUniqueRecommendationsID(): Flow<List<Int>>
}