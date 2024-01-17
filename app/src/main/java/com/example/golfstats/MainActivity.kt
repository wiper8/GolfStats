package com.example.golfstats

import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.tooling.preview.Preview
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.navigation
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.example.golfstats.ui.AppViewModelProvider
import com.example.golfstats.ui.Databases.DatabasesScreen
import com.example.golfstats.ui.Databases.DatabasesViewModel
import com.example.golfstats.ui.theme.GolfStatsTheme
import com.example.golfstats.ui.MenuScreen
import com.example.golfstats.ui.Sessions.SessionsScreen
import com.example.golfstats.ui.Sessions.SessionsViewModel
import com.example.golfstats.ui.Sessions.StatSessionsScreen
import com.example.golfstats.ui.Sessions.StatsViewModel
import com.example.golfstats.ui.Shots.ShotEvent
import com.example.golfstats.ui.Shots.ShotSessionScreen
import com.example.golfstats.ui.Shots.ShotViewModel
import com.example.golfstats.ui.Stats.StatEvent
import com.example.golfstats.ui.Stats.StatsScreen
import com.example.golfstats.ui.Yardages.YardageViewModel
import com.example.golfstats.ui.Yardages.YardagesScreen

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            GolfStatsTheme {
                GolfApp()
            }
        }
    }
}

enum class Screens(val title: String) {
    Menu("home"),
    Yardages("yardages"),
    Course("course"),
    Sessions("Sessions"),
    Range("range"),
    Stats("stats"),
    PlaySession("PlaySession"),
    PlaySessionRange("PlaySessionRange"),
    Databases("Databases")
}

@Preview(showBackground = true)
@Composable
fun GolfApp(
    navController: NavHostController = rememberNavController()
) {
    NavHost(
        navController = navController,
        startDestination = Screens.Menu.name
    ) {
        composable(Screens.Menu.name) {
            MenuScreen(navController)
        }

        navigation(
            startDestination = Screens.Databases.name,
            route = "databases_graph"
        ) {
            composable(Screens.Databases.name) {

                val viewModel: DatabasesViewModel = viewModel(factory = AppViewModelProvider.Factory)
                val state = viewModel.state.collectAsStateWithLifecycle()
                val onEvent = viewModel::onEvent

                DatabasesScreen(state.value, onEvent, navController)
            }
        }

        navigation(
            startDestination = Screens.Yardages.name,
            route = "yardages_graph"
        ) {
            composable(Screens.Yardages.name) {
                val viewModel: YardageViewModel = viewModel(factory = AppViewModelProvider.Factory)
                val state = viewModel.state.collectAsStateWithLifecycle()
                val onEvent = viewModel::onEvent

                YardagesScreen(state.value, onEvent, navController, viewModel.newRow)
            }
        }

        navigation(
            startDestination = Screens.Sessions.name,
            route="sessioncourse_graph"
        ) {
            composable(Screens.Sessions.name) {
                val viewModel: SessionsViewModel = viewModel(factory = AppViewModelProvider.Factory)
                val state = viewModel.state.collectAsStateWithLifecycle()
                val onEvent = viewModel::onEvent

                SessionsScreen(state.value, onEvent, viewModel.newRow, viewModel.newCourseHoles,
                    viewModel.newCourseRow, navController = navController, onNavClick = {it1, it2, it3 ->
                    navController.navigate(Screens.PlaySession.name + "/${it1}/${it2}/${it3}")
                }, range_only = false)
            }

            composable(Screens.PlaySession.name + "/{session_id}/{course_id}/{hole_num}",
                arguments = listOf(
                    navArgument("session_id") {
                        type = NavType.IntType
                    },
                    navArgument("course_id") {
                        type = NavType.IntType
                    },
                    navArgument("hole_num") {
                        type = NavType.IntType
                    }
                )) {
                val session_id = it.arguments?.getInt("session_id")
                val course_id = it.arguments?.getInt("course_id")
                val hole_num = it.arguments?.getInt("hole_num")
                if (session_id != null && hole_num != null) {
                    val viewModel: ShotViewModel = viewModel(factory = AppViewModelProvider.Factory)
                    val state = viewModel.state.collectAsStateWithLifecycle()
                    val onEvent = viewModel::onEvent

                    onEvent(ShotEvent.SetSessionId(session_id))
                    onEvent(ShotEvent.SetCourseId(course_id))
                    onEvent(ShotEvent.SetHoleNum(hole_num))

                    ShotSessionScreen(state, viewModel.newShotAvailable, viewModel.error_gen, onEvent, navController = navController)
                }
            }
        }

        navigation(
            startDestination = Screens.Range.name,
            route="range_graph"
        ) {
            composable(Screens.Range.name) {
                val viewModel: SessionsViewModel = viewModel(factory = AppViewModelProvider.Factory)
                val state = viewModel.state.collectAsStateWithLifecycle()
                val onEvent = viewModel::onEvent

                SessionsScreen(state.value, onEvent, viewModel.newRow, viewModel.newCourseHoles,
                    viewModel.newCourseRow, navController = navController, onNavClick = {it1, it2, it3 ->
                    navController.navigate(Screens.PlaySessionRange.name + "/${it1}/${it2}/${it3}")
                }, range_only = true)
            }

            composable(Screens.PlaySessionRange.name + "/{session_id}/{course_id}/{hole_num}",
                arguments = listOf(
                    navArgument("session_id") {
                        type = NavType.IntType
                    },
                    navArgument("course_id") {
                        type = NavType.IntType
                    },
                    navArgument("hole_num") {
                        type = NavType.IntType
                    }
                )) {
                val session_id = it.arguments?.getInt("session_id")
                val course_id = it.arguments?.getInt("course_id")
                val hole_num = it.arguments?.getInt("hole_num")
                if (session_id != null && hole_num != null) {
                    val viewModel: ShotViewModel = viewModel(factory = AppViewModelProvider.Factory)
                    val state = viewModel.state.collectAsStateWithLifecycle()
                    val onEvent = viewModel::onEvent

                    onEvent(ShotEvent.SetSessionId(session_id))
                    onEvent(ShotEvent.SetCourseId(course_id))
                    onEvent(ShotEvent.SetHoleNum(hole_num))

                    ShotSessionScreen(state, viewModel.newShotAvailable, viewModel.error_gen, onEvent, navController = navController)
                }
            }
        }

        navigation(
            startDestination = Screens.Sessions.name,
            route="stats_graph"
        ) {
            composable(Screens.Sessions.name) {
                val viewModel: SessionsViewModel = viewModel(factory = AppViewModelProvider.Factory)
                val state = viewModel.state.collectAsStateWithLifecycle()
                val onEvent = viewModel::onEvent

                StatSessionsScreen(state.value, onEvent, navController = navController, onNavClick = {
                    navController.navigate(Screens.Stats.name + "/${it}")
                })
            }
            composable(Screens.Stats.name + "/{session_id}",
                arguments = listOf(
                    navArgument("session_id") {
                        type = NavType.IntType
                    }
                )) {
                val session_id = it.arguments?.getInt("session_id")
                if (session_id != null) {
                    val viewModel: StatsViewModel = viewModel(factory = AppViewModelProvider.Factory)
                    val state = viewModel.state.collectAsStateWithLifecycle()
                    val onEvent = viewModel::onEvent

                    onEvent(StatEvent.Creation)
                    onEvent(StatEvent.SetSessionId(session_id))

                    StatsScreen(state, viewModel::onEvent, navController = navController)
                }
            }
        }
    }
}

/*
@Composable
inline fun <reified T : ViewModel> NavBackStackEntry.sharedViewModel(navController: NavController):T {
    val navGraphRoute = destination.parent?.route ?: return viewModel()
    val parentEntry = remember(this) {
        navController.getBackStackEntry(navGraphRoute)
    }
    return viewModel(parentEntry)
}
*/
