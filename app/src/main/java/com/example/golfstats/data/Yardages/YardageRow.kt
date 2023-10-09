package com.example.golfstats.data.Yardages

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity("yardages_table")
data class YardageRow(
    @PrimaryKey()
    var baton: String = "",
    var ninety: Int = 0,
    var hundred: Int = 0
)
