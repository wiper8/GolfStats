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

    onEvent(StatEvent.SetSessionId(session_id))
    onEvent(StatEvent.GetUniqueShotName(session_id))
    onEvent(StatEvent.GetStatSuccess(session_id))
    Log.d("EEEEE", "after event, success" + state.value.success.toString())
    onEvent(StatEvent.GetStatSuccessTry(session_id))
    //onEvent(StatEvent.GetStatPutt(session_id))

    Column {
        Row {
            Text("Shot")
            Text("Success")
            Text("Green")
            Text("Penalty")
            Text("Reset")
        }
        LazyColumn() {
            itemsIndexed(state.value.uniqueshotsname) {index, item ->
                Log.d("EEEEE", "inside lazy with item " + index.toString())
                Log.d("EEEEE", state.value.uniqueshotsname.toString())
                Log.d("EEEEE", state.value.success.toString())
                StatItem(state.value.uniqueshotsname[index],
                    state.value.success[index], 0,
                    0,0,0,0,0,0)
                    //state.value.green[index], state.value.greenTry[index],
                    //state.value.penalty[index], state.value.penaltyTry[index],
                    //state.value.reset[index], state.value.resetTry[index])
            }
        }
        PuttStat(one = 0, two = 0, three = 0, four = 0, five = 0)
        Button(onClick = {
            navController.popBackStack(route = Screens.Stats.name, inclusive = false)
        }) {
            Icon(Icons.Default.ArrowBack, "return")
        }
    }
}

@Composable
fun StatItem(shot: String, success: Int, successTry: Int, green: Int, greenTry: Int,
             penalty: Int, penaltyTry: Int, reset: Int, resetTry: Int) {
    Row {
        Text(shot)
        Text(" ${success.toString()} / ${successTry.toString()}")

    }
}

@Composable
fun PuttStat(one: Int, two: Int, three: Int, four: Int, five: Int) {
    Column {
        Row {
            Text("1")
            Text("2")
            Text("3")
            Text("4")
            Text("5")
        }
        Row {
            Text("${one.toString()}")
            Text("${two.toString()}")
            Text("${three.toString()}")
            Text("${four.toString()}")
            Text("${five.toString()}")
        }
    }
}