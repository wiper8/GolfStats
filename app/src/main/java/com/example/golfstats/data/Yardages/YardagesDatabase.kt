package com.example.golfstats.data.Yardages

import android.content.Context
import android.util.Log
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase

@Database(
    entities = [YardageRow::class],
    version = 1,
    exportSchema = false
)
abstract class YardagesDatabase: RoomDatabase() {
    abstract fun yardageDao(): YardagesDao

    companion object {
        @Volatile
        private var Instance: YardagesDatabase? = null

        fun getDatabase(context: Context): YardagesDatabase {

            // if the Instance is not null, return it, otherwise create a new database instance.
            return Instance ?: synchronized(this) {
                Room.databaseBuilder(context, YardagesDatabase::class.java, "yardages_table")
                    .build()
                    .also { Instance = it }
            }
        }
    }
}