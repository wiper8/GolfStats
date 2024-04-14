package com.example.golfstats.data.Recommendations

import android.content.Context
import android.util.Log
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase

@Database(
    entities = [RecommendationRow::class],
    version = 1,
    exportSchema = false
)
abstract class RecommendationsDatabase: RoomDatabase() {
    abstract fun recommendationsDao(): RecommendationsDao

    companion object {

        @Volatile
        private var Instance: RecommendationsDatabase? = null

        fun getDatabase(context: Context): RecommendationsDatabase {
            // if the Instance is not null, return it, otherwise create a new database instance.
            return Instance ?: synchronized(this) {
                Room.databaseBuilder(context, RecommendationsDatabase::class.java, "recommendations_table")
                    .build()
                    .also { Instance = it }
            }
        }
    }
}
