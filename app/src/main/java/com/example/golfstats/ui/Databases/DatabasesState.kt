package com.example.golfstats.ui.Databases

import com.example.golfstats.data.Course.CourseRow
import com.example.golfstats.data.Holes.HoleRow
import com.example.golfstats.data.Sessions.SessionRow
import com.example.golfstats.data.Shots.ShotRow
import com.example.golfstats.data.ShotsAvailable.ShotsAvailableRow
import com.example.golfstats.data.Yardages.YardageRow

data class DatabasesState(
    val which_db_open: Int = 0,
    val courses: List<CourseRow> = listOf(),
    val holes: List<HoleRow> = listOf(),
    val shots: List<ShotRow> = listOf(),
    val sessions: List<SessionRow> = listOf()
)