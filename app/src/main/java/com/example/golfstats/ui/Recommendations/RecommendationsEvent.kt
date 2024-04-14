package com.example.golfstats.ui.Recommendations

import com.example.golfstats.data.Recommendations.RecommendationRow
import com.example.golfstats.data.ShotsAvailable.ShotsAvailableRow


sealed interface RecommendationsEvent {
    object OnAddNewClick: RecommendationsEvent //ajouter une nouvelle recommendation pour un trou
    object DismissRecommend: RecommendationsEvent //annuler cette nouvelle recommendations pour un trou
    object SaveRecommendation: RecommendationsEvent //enregistrer cette nouvelle recommendation pour un trou
    data class SetHoleNum(val num: Int): RecommendationsEvent
    data class SetHoleId(val id: Int): RecommendationsEvent
    data class OnClickShot(val shot: ShotsAvailableRow): RecommendationsEvent
    data class OnChangeExpectInt(val expect: String): RecommendationsEvent
    data class OnChangeExpectFloat(val expect: String): RecommendationsEvent
    data class DeleteRecommend(val l: List<RecommendationRow>): RecommendationsEvent
}