package com.example.golfstats.data.Sessions

//import ShotRow
import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Transaction
import androidx.room.Upsert
import com.example.golfstats.data.Course.CourseRow
//import com.example.golfstats.data.SessionWithShots.SessionWithShots
import kotlinx.coroutines.flow.Flow

@Dao
interface SessionsDao {
    @Upsert
    suspend fun upsert(sessionrow: SessionRow)

    @Delete
    suspend fun delete(sessionrow: SessionRow)

    @Query("SELECT * FROM sessions_table ORDER BY id DESC")
    fun getSessions(): Flow<List<SessionRow>>

    @Query("SELECT * FROM sessions_table WHERE date = :date")
    fun getSessionIdFromDate(date: String): Flow<SessionRow>

    @Query("SELECT * FROM sessions_table WHERE type = 'course' ORDER BY id DESC")
    fun getCourseSessions(): Flow<List<SessionRow>>

    @Query("SELECT * FROM sessions_table WHERE type = 'range' ORDER BY id DESC")
    fun getRangeSessions(): Flow<List<SessionRow>>

    @Query("SELECT * from sessions_table WHERE id = :id")
    fun getItem(id: Int): Flow<SessionRow>

    @Query("SELECT * from sessions_table WHERE date = :date")
    fun getItemByDate(date: String): SessionRow

    //@Insert
    //suspend fun insertShot(row: ShotRow)

    //@Transaction
    //@Query("SELECT * FROM sessions WHERE id = :session_id")
    //suspend fun getSessionWithShots(session_id: Int): List<SessionWithShots>
}