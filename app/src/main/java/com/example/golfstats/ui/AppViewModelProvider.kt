/*
 * Copyright (C) 2023 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     https://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.example.golfstats.ui

import androidx.lifecycle.ViewModelProvider.AndroidViewModelFactory
import androidx.lifecycle.viewmodel.CreationExtras
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import com.example.golfstats.GolfStatsApplication
import com.example.golfstats.data.Recommendations.RecommendationsRepo
import com.example.golfstats.data.ShotsAvailable.ShotsAvailableRepo
import com.example.golfstats.ui.Databases.DatabasesViewModel
import com.example.golfstats.ui.Recommendations.RecommendationsViewModel
import com.example.golfstats.ui.Shots.ShotViewModel
import com.example.golfstats.ui.Sessions.SessionsViewModel
import com.example.golfstats.ui.Sessions.StatsViewModel
import com.example.golfstats.ui.Yardages.YardageViewModel

/**
 * Provides Factory to create instance of ViewModel for the entire Inventory app
 */
object AppViewModelProvider {
    val Factory = viewModelFactory {
        initializer {
            YardageViewModel(golfstatsApplication().container.yardagesRepo)
        }
        initializer {
            SessionsViewModel(golfstatsApplication().container.sessionsRepo,
                golfstatsApplication().container.shotsRepo,
                golfstatsApplication().container.courseRepo,
                golfstatsApplication().container.holesRepo)
        }
        initializer {
            ShotViewModel(golfstatsApplication().container.shotsRepo,
                golfstatsApplication().container.shotsavailableRepo,
                golfstatsApplication().container.sessionsRepo,
                golfstatsApplication().container.recommendationsRepo)
        }
        initializer {
            StatsViewModel(golfstatsApplication().container.shotsRepo, golfstatsApplication().container.shotsavailableRepo)
        }
        initializer {
            DatabasesViewModel(
                golfstatsApplication().container.courseRepo,
                golfstatsApplication().container.holesRepo,
                golfstatsApplication().container.shotsRepo,
                golfstatsApplication().container.sessionsRepo
            )
        }
        initializer {
            RecommendationsViewModel(
                golfstatsApplication().container.holesRepo,
                golfstatsApplication().container.shotsavailableRepo,
                golfstatsApplication().container.recommendationsRepo
            )
        }
    }
}

//fun CreationExtras.golfstatsApplication(): GolfStatsApplication =
//    (this[AndroidViewModelFactory.APPLICATION_KEY] as GolfStatsApplication)

fun CreationExtras.golfstatsApplication(): GolfStatsApplication {
    val application = this[AndroidViewModelFactory.APPLICATION_KEY]

    if (application is GolfStatsApplication) {
        return application
    } else {
        throw IllegalStateException("The application is not of type InventoryApplication")
    }
}
