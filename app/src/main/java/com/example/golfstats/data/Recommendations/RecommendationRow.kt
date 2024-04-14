package com.example.golfstats.data.Recommendations

import androidx.room.Entity
import androidx.room.PrimaryKey


@Entity("recommendations_table")
data class RecommendationRow(
    @PrimaryKey(autoGenerate = true)
    var id: Int = 0,
    var recommend_id: Int = 0,
    var num: Int = 0,
    var shot_id: Int = 0,
    var hole_id: Int = 0,
    var expectency: Double = 0.0
)