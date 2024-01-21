package com.example.golfstats.data.Recommendations

import kotlinx.coroutines.flow.Flow

interface RecommendationsRepo {
    fun getRecommendations(): Flow<List<RecommendationRow>>
    fun getRecommendationsHole(hole_id: Int, recommend_id: Int): Flow<List<RecommendationRow>>
    fun getUniqueRecommendationsHole(hole_id: Int): Flow<List<Int>>
    fun getUniqueRecommendationsID(): Flow<List<Int>>
    suspend fun upsert(row: RecommendationRow)
    suspend fun delete(row: RecommendationRow)
}