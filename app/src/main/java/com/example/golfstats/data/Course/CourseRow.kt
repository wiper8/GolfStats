package com.example.golfstats.data.Course

import androidx.room.Entity
import androidx.room.PrimaryKey


@Entity("courses")
data class CourseRow(
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,
    val nom: String = "",
    val holes: Int = 18
)
