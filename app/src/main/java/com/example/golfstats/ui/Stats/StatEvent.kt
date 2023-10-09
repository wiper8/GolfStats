package com.example.golfstats.ui.Stats

import com.example.golfstats.ui.Shots.ShotEvent

sealed interface StatEvent {
    data class SetSessionId(val session_id: Int): StatEvent
    data class GetUniqueShotName(val session_id: Int): StatEvent
    data class GetStatSuccess(val session_id: Int): StatEvent
    data class GetStatSuccessTry(val session_id: Int): StatEvent
    data class GetStatGreen(val session_id: Int): StatEvent
    data class GetStatGreenTry(val session_id: Int): StatEvent
    data class GetStatPenalty(val session_id: Int): StatEvent
    data class GetStatPenaltyTry(val session_id: Int): StatEvent
    data class GetStatReset(val session_id: Int): StatEvent
    data class GetStatResetTry(val session_id: Int): StatEvent
    data class GetStatPutt(val session_id: Int): StatEvent
}