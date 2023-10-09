package com.example.golfstats.ui.Course

import com.example.golfstats.data.Course.CourseRow


data class CourseState(
    val coursesList: List<CourseRow> = listOf(),
    var current_row: CourseRow = CourseRow(),
    var is_new_screen_open: Boolean = false
)