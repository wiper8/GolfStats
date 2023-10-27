package com.example.golfstats.ui.Sessions

import com.example.golfstats.data.Course.CourseRow
import com.example.golfstats.data.Sessions.SessionRow
import com.example.golfstats.data.Shots.ShotRow

data class SessionsState(
    val sessionsList: List<SessionRow> = listOf(),
    val allShots: List<ShotRow> = listOf(),
    val allCourses: List<CourseRow> = listOf(),
    //var current_row: SessionRow = SessionRow(),
    var course_id_from_current_row: Int = 0,
    var is_new_screen_open: Boolean = false,
    var is_new_course_screen_open: Boolean = false
)