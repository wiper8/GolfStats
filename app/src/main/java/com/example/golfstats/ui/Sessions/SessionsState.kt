package com.example.golfstats.ui.Sessions

import com.example.golfstats.data.Course.CourseRow
import com.example.golfstats.data.Holes.HoleRow
import com.example.golfstats.data.Recommendations.RecommendationRow
import com.example.golfstats.data.Sessions.SessionRow
import com.example.golfstats.data.Shots.ShotRow

data class SessionsState(
    val session_id: Int = 0,
    val course_id: Int = 0,
    val total_shots: Int = 0,
    val total_shots_under_par: Int = 0,
    val sessionsList: List<SessionRow> = listOf(),
    val allCourses: List<CourseRow> = listOf(),
    val allHoles: List<HoleRow> = listOf(),
    val allShots: List<ShotRow> = listOf(),
    val recomm_list: List<RecommendationRow> = listOf(),
    val scores_holes: List<Int> = listOf(),
    val holesCurr_Course: List<HoleRow> = listOf(),
    var is_new_screen_open: Boolean = false,
    var is_new_course_screen_open: Boolean = false,
    var is_card_screen_open: Boolean = false,
    var is_settings_open: Boolean = false,
    var is_score_total_visible: Boolean = false,
    var are_shots_calculated: Boolean = false,
    var is_session_id_found: Boolean = false,
    var is_add_recommendations_screen_open: Boolean = false
)