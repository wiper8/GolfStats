package com.example.golfstats.data.Sessions

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase

@Database(
    entities = [SessionRow::class],
    version = 1,
    exportSchema = false
)
abstract class SessionsDatabase: RoomDatabase() {
    abstract fun sessionsDao(): SessionsDao

    companion object {
        @Volatile
        private var Instance: SessionsDatabase? = null

        fun getDatabase(context: Context): SessionsDatabase {
            // if the Instance is not null, return it, otherwise create a new database instance.
            return Instance ?: synchronized(this) {
                Room.databaseBuilder(context, SessionsDatabase::class.java, "sessions_table")
                    .build()
                    .also { Instance = it }
            }
        }
    }
}