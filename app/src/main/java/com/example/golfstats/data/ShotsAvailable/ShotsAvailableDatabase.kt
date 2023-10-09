package com.example.golfstats.data.ShotsAvailable

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.example.golfstats.data.Shots.ShotsDao

@Database(
    entities = [ShotsAvailableRow::class],
    version = 1,
    exportSchema = false
)
abstract class ShotsAvailableDatabase: RoomDatabase() {
    abstract fun shotsAvailableDao(): ShotsAvailableDao

    companion object {
        @Volatile
        private var Instance: ShotsAvailableDatabase? = null

        fun getDatabase(context: Context): ShotsAvailableDatabase {
            // if the Instance is not null, return it, otherwise create a new database instance.
            return Instance ?: synchronized(this) {
                Room.databaseBuilder(context, ShotsAvailableDatabase::class.java, "shotsavailable_table")
                    .build()
                    .also { Instance = it }
            }
        }
    }
}