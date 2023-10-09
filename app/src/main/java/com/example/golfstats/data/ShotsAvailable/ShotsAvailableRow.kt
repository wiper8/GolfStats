package com.example.golfstats.data.ShotsAvailable

import androidx.room.Entity
import androidx.room.PrimaryKey


@Entity("shotsavailable_table")
data class ShotsAvailableRow(
    @PrimaryKey()
    var shot: String = "",
    var id: Int = 0,
    var green: Boolean = true,
    var penalty: Boolean = true,
    var reset: Boolean = true,
    var is_putt: Boolean = false
)