package com.example.golfstats.data.Course

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.example.golfstats.data.Holes.HoleRow


@Entity("courses")
data class CourseRow(
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,
    val nom: String = "",
    val holes: Int = 0
)
