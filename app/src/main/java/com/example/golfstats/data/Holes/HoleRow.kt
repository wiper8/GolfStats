package com.example.golfstats.data.Holes

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity("holes")
data class HoleRow(
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,
    val course_id: Int = 0,
    val numero: Int = 0,
    var yards: Int = 0,
    var par: Int = 0,
    val mypar: Int = 0,
    val myhandicap: Int = 0,
    val plan: String = "",
    val plan_safe: String = "",
    val plan_me: String = ""
)
