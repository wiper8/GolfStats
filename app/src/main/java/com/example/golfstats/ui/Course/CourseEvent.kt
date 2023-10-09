package com.example.golfstats.ui.Course

import com.example.golfstats.data.Course.CourseRow


sealed interface CourseEvent {
    object OnAddNewClick: CourseEvent
    object Dismiss: CourseEvent
    data class OnChangednom(val nom: String): CourseEvent
    object Save: CourseEvent
    data class Edit(val row: CourseRow): CourseEvent
    data class Delete(val row: CourseRow): CourseEvent
}