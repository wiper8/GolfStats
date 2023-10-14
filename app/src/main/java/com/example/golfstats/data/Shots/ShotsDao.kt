package com.example.golfstats.data.Shots

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Query
import androidx.room.Upsert
import kotlinx.coroutines.flow.Flow


@Dao
interface ShotsDao {
    @Upsert
    suspend fun upsert(row: ShotRow)

    @Delete
    suspend fun delete(row: ShotRow)

    @Query("SELECT * FROM shots_table ORDER BY id DESC")
    fun getShots(): Flow<List<ShotRow>>

    @Query("SELECT * from shots_table WHERE id = :id")
    fun getItem(id: Int): Flow<ShotRow>

    @Query("SELECT * FROM shots_table WHERE session_id = :session_id ORDER BY id DESC")
    fun getSessionShots(session_id: Int): Flow<List<ShotRow>>

    @Query("SELECT DISTINCT shot FROM shots_table")
    fun getUniqueAllShots(): Flow<List<String>>

    @Query("SELECT DISTINCT shot FROM shots_table WHERE session_id = :session_id")
    fun getUniqueShots(session_id: Int): Flow<List<String>>



    //@Query("SELECT COUNT(1) FROM shots_table WHERE session_id = :session_id AND success = 2 AND shot = :baton")
    //fun getSuccess(session_id: Int, baton: String): Flow<List<Int>>

    //@Query("SELECT COUNT(1) FROM shots_table WHERE session_id = :session_id AND (success = 2 OR success = 0) AND shot = :baton")
    //fun getSuccessTry(session_id: Int, baton: String): Flow<Int>

}