package com.example.golfstats.data.Course

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase

@Database(
    entities = [CourseRow::class],
    version = 1,
    exportSchema = false
)
abstract class CourseDatabase: RoomDatabase() {
    abstract fun courseDao(): CourseDao

    companion object {
        @Volatile
        private var Instance: CourseDatabase? = null

        fun getDatabase(context: Context): CourseDatabase {
            // if the Instance is not null, return it, otherwise create a new database instance.
            return Instance ?: synchronized(this) {
                Room.databaseBuilder(context, CourseDatabase::class.java, "courses")
                    .build()
                    .also { Instance = it }
            }
        }
    }
}