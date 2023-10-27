package com.example.golfstats

import CourseViewModel
import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.tooling.preview.Preview
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.navigation
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.example.golfstats.ui.AppViewModelProvider
import com.example.golfstats.ui.Course.CourseEvent
import com.example.golfstats.ui.Course.NewCourseRowScreen
import com.example.golfstats.ui.theme.GolfStatsTheme
import com.example.golfstats.ui.MenuScreen
import com.example.golfstats.ui.Sessions.SessionsScreen
import com.example.golfstats.ui.Sessions.StatSessionsScreen
import com.example.golfstats.ui.Sessions.StatsViewModel
import com.example.golfstats.ui.Shots.ShotEvent
import com.example.golfstats.ui.Shots.ShotSessionScreen
import com.example.golfstats.ui.Shots.ShotViewModel
import com.example.golfstats.ui.Stats.StatEvent
import com.example.golfstats.ui.Stats.StatsScreen
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
    NewCourse("newcourse"),
    Sessions("Sessions"),
    Stats("stats"),
    PlaySession("PlaySession")
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
            startDestination = Screens.Yardages.name,
            route = "yardages_graph"
        ) {
            composable(Screens.Yardages.name) {
                YardagesScreen(navController)
            }
        }

        navigation(
            startDestination = Screens.Course.name,
            route="course_graph"
        ) {
            composable(Screens.Course.name) {
                SessionsScreen(navController = navController, onNavClick = {
                    navController.navigate(Screens.PlaySession.name + "/${it}")
                }, range_only = false)
            }
            composable(Screens.NewCourse.name + "/{course_id}",
                arguments = listOf(
                    navArgument("course_id") {
                        type = NavType.IntType
                    }
                )) {
                val course_id = it.arguments?.getInt("course_id")
                if (course_id != null) {
                    val viewModel: CourseViewModel = viewModel(factory = AppViewModelProvider.Factory)
                    val state = viewModel.state.collectAsState()
                    val onEvent = viewModel::onEvent
                    Log.d("EEEEE", "setting course id")
                    onEvent(CourseEvent.SetCourseId(course_id))
                    Log.d("EEEEE", "set! opening newCoursescreen")
                    NewCourseRowScreen(state, viewModel.newRow, onEvent, navController = navController)
                }
            }
        }

        navigation(
            startDestination = Screens.Sessions.name,
            route="range_graph"
        ) {
            composable(Screens.Sessions.name) {
                SessionsScreen(navController = navController, onNavClick = {
                    navController.navigate(Screens.PlaySession.name + "/${it}")
                }, range_only = true)
            }
            composable(Screens.PlaySession.name + "/{session_id}",
                arguments = listOf(
                    navArgument("session_id") {
                        type = NavType.IntType
                    }
                )) {
                val session_id = it.arguments?.getInt("session_id")
                if (session_id != null) {
                    val viewModel: ShotViewModel = viewModel(factory = AppViewModelProvider.Factory)
                    val state = viewModel.state.collectAsState()
                    val onEvent = viewModel::onEvent

                    onEvent(ShotEvent.SetSessionId(session_id))

                    ShotSessionScreen(state, viewModel.newShotAvailable, viewModel.error_gen, onEvent, navController = navController)
                }
            }
        }

        navigation(
            startDestination = Screens.Sessions.name,
            route="stats_graph"
        ) {
            composable(Screens.Sessions.name) {
                StatSessionsScreen(navController = navController, onNavClick = {
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
                    val state = viewModel.state.collectAsState()
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
