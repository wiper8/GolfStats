package com.example.golfstats.ui.Sessions

import com.example.golfstats.data.Sessions.SessionRow

data class SessionsState(
    val sessionsList: List<SessionRow> = listOf(),
    //var current_row: SessionRow = SessionRow(),
    var course_id_from_current_row: Int = 0,
    var is_new_screen_open: Boolean = false
)