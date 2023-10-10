package com.example.golfstats.ui.Stats

import android.util.Log
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.Button
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.State
import androidx.compose.runtime.collectAsState
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import com.example.golfstats.Screens
import com.example.golfstats.ui.AppViewModelProvider
import com.example.golfstats.ui.Sessions.StatsViewModel
import com.example.golfstats.ui.Shots.ShotEvent
import com.example.golfstats.ui.Shots.ShotViewModel

@Composable
fun StatsScreen(session_id: Int, navController: NavHostController) {
    val viewModel: StatsViewModel = viewModel(factory = AppViewModelProvider.Factory)
    val state = viewModel.state.collectAsState()
    val onEvent = viewModel::onEvent

    onEvent(StatEvent.Creation)
    onEvent(StatEvent.SetSessionId(session_id))
    onEvent(StatEvent.GetStats)

    Row {
        Column {
            Text("Shot"+" ".repeat(maxOf(state.value.longest_shot_name-4, 0)))
            LazyColumn() {
                itemsIndexed(state.value.uniqueshotsname) {index, item ->
                    if(item != "Putt") {
                        Text(state.value.uniqueshotsname[index] +
                                " ".repeat(maxOf(state.value.longest_shot_name-3, 1)))
                    }
                }
            }
        }

        Column {
            Row {
                Text(" Success ")
                Text("Green  ")
                Text("Penalty ")
                Text("Reset")
            }

            LazyColumn() {
                itemsIndexed(state.value.uniqueshotsname) {index, item ->
                    if(item != "Putt") {
                        StatItem(state.value.uniqueshotsname[index],
                            state.value.success[index], state.value.successTry[index],
                            state.value.green[index], state.value.greenTry[index],
                            state.value.penalty[index], state.value.penaltyTry[index],
                            state.value.reset[index], state.value.resetTry[index], state.value.longest_shot_name)
                    }
                }
            }
            PuttStat(one = state.value.putts[0], two = state.value.putts[1], three = state.value.putts[2],
                four = state.value.putts[3], five = state.value.putts[4])
            Button(onClick = {
                navController.popBackStack(route = Screens.Sessions.name, inclusive = false)
            }) {
                Icon(Icons.Default.ArrowBack, "return")
            }
        }
    }
}

@Composable
fun StatItem(shot: String, success: Int, successTry: Int, green: Int, greenTry: Int,
             penalty: Int, penaltyTry: Int, reset: Int, resetTry: Int, longest: Int) {
    Text("    ${success.toString()} / ${successTry.toString()}     ${green.toString()} / " +
            "${greenTry.toString()}   ${penalty.toString()} / ${penaltyTry.toString()}    ${reset.toString()} / ${resetTry.toString()}")
}

@Composable
fun PuttStat(one: Int, two: Int, three: Int, four: Int, five: Int) {
    Column {
        Row{
            Text("Putt")
        }
        Text("1 2 3 4 5")
        Row {
            Text("${one.toString()} ")
            Text("${two.toString()} ")
            Text("${three.toString()} ")
            Text("${four.toString()} ")
            Text("${five.toString()}")
        }
    }
}