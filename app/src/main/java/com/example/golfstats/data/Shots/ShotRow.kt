package com.example.golfstats.data.Shots

import androidx.room.Entity
import androidx.room.PrimaryKey


@Entity("shots_table")
data class ShotRow(
    @PrimaryKey(autoGenerate = true)
    var id: Int = 0,
    var num_hole: Int = 0,
    var shot: String = "",
    var success: Int = 0,
    var green: Int = 0,
    var penalty: Int = 0,
    var reset: Int = 0,
    var is_putt: Boolean = false,
    val session_id: Int = -1
)