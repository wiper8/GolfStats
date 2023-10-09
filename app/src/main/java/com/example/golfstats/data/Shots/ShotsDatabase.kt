package com.example.golfstats.data.Shots

import android.content.Context
import android.util.Log
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase

@Database(
    entities = [ShotRow::class],
    version = 1,
    exportSchema = false
)
abstract class ShotsDatabase: RoomDatabase() {
    abstract fun shotsDao(): ShotsDao

    companion object {

        @Volatile
        private var Instance: ShotsDatabase? = null

        fun getDatabase(context: Context): ShotsDatabase {
            // if the Instance is not null, return it, otherwise create a new database instance.
            return Instance ?: synchronized(this) {
                Room.databaseBuilder(context, ShotsDatabase::class.java, "shots_table")
                    .build()
                    .also { Instance = it }
            }
        }
    }
}
