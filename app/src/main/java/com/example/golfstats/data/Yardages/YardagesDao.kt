package com.example.golfstats.data.Yardages

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Query
import androidx.room.Upsert
import kotlinx.coroutines.flow.Flow

@Dao
interface YardagesDao {
    @Upsert
    suspend fun upsert(yardagerow: YardageRow)

    @Delete
    suspend fun delete(yardagerow: YardageRow)

    @Query("SELECT * FROM yardages_table ORDER BY hundred DESC")
    fun getYardages(): Flow<List<YardageRow>>

    @Query("SELECT * from yardages_table WHERE baton = :baton")
    fun getItem(baton: String): Flow<YardageRow>
}