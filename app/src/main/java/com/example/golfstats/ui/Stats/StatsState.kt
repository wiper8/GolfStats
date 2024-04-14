package com.example.golfstats.ui.Stats

import com.example.golfstats.data.Shots.ShotRow
import com.example.golfstats.data.ShotsAvailable.ShotsAvailableRow

data class StatsState(
    val session_id: Int = 0,
    val shotsList: List<ShotRow> = listOf(),
    val shotsAvailableList: List<ShotsAvailableRow> = listOf(),
    val uniqueshotsname: List<String> = listOf(),
    val success: List<Int> = listOf(),
    val successTry: List<Int> = listOf(),
    val green: List<Int> = listOf(),
    val greenTry: List<Int> = listOf(),
    val penalty: List<Int> = listOf(),
    val penaltyTry: List<Int> = listOf(),
    val reset: List<Int> = listOf(),
    val resetTry: List<Int> = listOf(),
    val putts: List<Int> = listOf(0, 0, 0, 0, 0),
    val longest_shot_name: Int = 0
)