package com.example.golfstats.data.Sessions

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity("sessions_table")
data class SessionRow(
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,
    val date: String = "",
    val type: String = "range",
    val course_id: Int = 0
)
