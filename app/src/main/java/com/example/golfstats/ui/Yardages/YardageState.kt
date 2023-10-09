package com.example.golfstats.ui.Yardages

import com.example.golfstats.data.Yardages.YardageRow

data class YardageState(
    val yardageList: List<YardageRow> = listOf(),
    var current_row: YardageRow = YardageRow(),
    var is_new_screen_open: Boolean = false
)