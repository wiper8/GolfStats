package com.example.golfstats

import android.app.Application
import com.example.golfstats.data.AppContainer
import com.example.golfstats.data.AppDataContainer

class GolfStatsApplication : Application() {

    lateinit var container: AppContainer

    override fun onCreate() {
        super.onCreate()
        container = AppDataContainer(this)
    }
}
