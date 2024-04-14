package com.example.golfstats.ui.Recommendations

import com.example.golfstats.data.Course.CourseRow
import com.example.golfstats.data.Holes.HoleRow
import com.example.golfstats.data.Recommendations.RecommendationRow
import com.example.golfstats.data.ShotsAvailable.ShotsAvailableRow

data class RecommendationsState(
    val hole_num: Int = 0,
    val hole_id: Int = 0,
    val allCourses: List<CourseRow> = listOf(),
    val allHoles: List<HoleRow> = listOf(),
    val allShotsAvailable: List<ShotsAvailableRow> = listOf(),
    val allRecommendations: List<RecommendationRow> = listOf(),
    val allRecommendationsID: List<Int> = listOf(),
    var is_choix_open: Boolean = false
)