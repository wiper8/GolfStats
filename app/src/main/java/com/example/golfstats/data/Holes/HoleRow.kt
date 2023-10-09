package com.example.golfstats.data.Holes

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity("holes")
data class HoleRow(
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,
    val numero: Int = 1,
    val yards: Int = 0,
    val par: Int = 4,
    val mypar: Int = 5,
    val myhandicap: Int = 9,
    val plan: String = "",
    val plan_safe: String = "",
    val plan_me: String = ""
)
