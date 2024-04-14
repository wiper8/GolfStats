package com.example.golfstats.data.Recommendations

import kotlinx.coroutines.flow.Flow

class RecommendationsOffRepo(private val recommendationsDao: RecommendationsDao): RecommendationsRepo {
    override fun getRecommendations(): Flow<List<RecommendationRow>> = recommendationsDao.getRecommendations()
    override fun getRecommendationsHole(hole_id: Int, recommend_id: Int): Flow<List<RecommendationRow>> = recommendationsDao.getRecommendationsHole(hole_id, recommend_id)
    override fun getUniqueRecommendationsHole(hole_id: Int): Flow<List<Int>> = recommendationsDao.getUniqueRecommendationsHole(hole_id)
    override fun getUniqueRecommendationsID(): Flow<List<Int>> = recommendationsDao.getUniqueRecommendationsID()
    override suspend fun upsert(row: RecommendationRow) = recommendationsDao.upsert(row)
    override suspend fun delete(row: RecommendationRow) = recommendationsDao.delete(row)

}