package com.example.golfstats.ui.Stats

import com.example.golfstats.data.Shots.ShotRow

data class StatsState(
    val session_id: Int = 0,
    val shotsList: List<ShotRow> = listOf(),
    val uniqueshotsname: List<String> = listOf(),
    val success: List<Int> = listOf(),
    val successTry: List<Int> = listOf(),
    val green: List<Int> = listOf(),
    val greenTry: List<Int> = listOf(),
    val penalty: List<Int> = listOf(),
    val penaltyTry: List<Int> = listOf(),
    val reset: List<Int> = listOf(),
    val resetTry: List<Int> = listOf(),
    val putts: List<Int> = listOf()
)