package com.example.golfstats.ui.Stats


sealed interface StatEvent {
    data class SetSessionId(val session_id: Int): StatEvent
    object GetStats: StatEvent
    object Creation: StatEvent
}