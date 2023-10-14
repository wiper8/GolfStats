package com.example.golfstats.ui.Stats


sealed interface StatEvent {
    data class SetSessionId(val session_id: Int): StatEvent
    object Creation: StatEvent
    object GetStats: StatEvent
}