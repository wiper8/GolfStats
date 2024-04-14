package com.example.golfstats.ui.Databases


sealed interface DatabasesEvent {
    object ClickCourses: DatabasesEvent
    object ClickHoles: DatabasesEvent
    object ClickShots: DatabasesEvent
    object ClickSessions: DatabasesEvent
    object Dismiss: DatabasesEvent
}