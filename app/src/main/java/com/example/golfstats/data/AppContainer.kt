package com.example.golfstats.data

import android.content.Context
import com.example.golfstats.data.Course.CourseDatabase
import com.example.golfstats.data.Course.CourseOffRepo
import com.example.golfstats.data.Course.CourseRepo
import com.example.golfstats.data.Holes.HolesDatabase
import com.example.golfstats.data.Holes.HolesOffRepo
import com.example.golfstats.data.Holes.HolesRepo
import com.example.golfstats.data.Recommendations.RecommendationsDatabase
import com.example.golfstats.data.Recommendations.RecommendationsOffRepo
import com.example.golfstats.data.Recommendations.RecommendationsRepo
import com.example.golfstats.data.Sessions.SessionsDatabase
import com.example.golfstats.data.Sessions.SessionsOffRepo
import com.example.golfstats.data.Sessions.SessionsRepo
import com.example.golfstats.data.ShotsAvailable.ShotsAvailableRepo
import com.example.golfstats.data.Shots.ShotsDatabase
import com.example.golfstats.data.Shots.ShotsOffRepo
import com.example.golfstats.data.Shots.ShotsRepo
import com.example.golfstats.data.ShotsAvailable.ShotsAvailableDatabase
import com.example.golfstats.data.ShotsAvailable.ShotsAvailableOffRepo
import com.example.golfstats.data.Yardages.YardagesDatabase
import com.example.golfstats.data.Yardages.YardagesOffRepo
import com.example.golfstats.data.Yardages.YardagesRepo

interface AppContainer {
    val yardagesRepo: YardagesRepo
    val sessionsRepo: SessionsRepo
    val shotsRepo: ShotsRepo
    val shotsavailableRepo : ShotsAvailableRepo
    val courseRepo: CourseRepo
    val holesRepo: HolesRepo
    val recommendationsRepo: RecommendationsRepo
}

class AppDataContainer(private val context: Context) : AppContainer {
    override val yardagesRepo: YardagesRepo by lazy {
        YardagesOffRepo(YardagesDatabase.getDatabase(context).yardageDao())
    }
    override val sessionsRepo: SessionsRepo by lazy {
        SessionsOffRepo(SessionsDatabase.getDatabase(context).sessionsDao())
    }
    override val shotsRepo: ShotsRepo by lazy {
        ShotsOffRepo(ShotsDatabase.getDatabase(context).shotsDao())
    }
    override val shotsavailableRepo: ShotsAvailableRepo by lazy {
        ShotsAvailableOffRepo(ShotsAvailableDatabase.getDatabase(context).shotsAvailableDao())
    }
    override val courseRepo: CourseRepo by lazy {
        CourseOffRepo(CourseDatabase.getDatabase(context).courseDao())
    }
    override val holesRepo: HolesRepo by lazy {
        HolesOffRepo(HolesDatabase.getDatabase(context).dao())
    }
    override val recommendationsRepo: RecommendationsRepo by lazy {
        RecommendationsOffRepo(RecommendationsDatabase.getDatabase(context).recommendationsDao())
    }
}