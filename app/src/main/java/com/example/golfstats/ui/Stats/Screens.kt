package com.example.golfstats.ui.Stats

import android.util.Log
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.Button
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.State
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import com.example.golfstats.Screens

@Composable
fun StatsScreen(state: State<StatsState>, onEvent: (StatEvent) -> Unit, navController: NavHostController) {

    onEvent(StatEvent.GetStats)

    Column(
        Modifier.fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Row {
            Column {
                Text(
                    "Shot" + " ".repeat(maxOf(state.value.longest_shot_name - 4, 0)),
                    fontSize = 20.sp
                )
                LazyColumn() {
                    itemsIndexed(state.value.uniqueshotsname) { index, item ->
                        if (item != "Putt") {
                            Text(
                                state.value.uniqueshotsname[index] +
                                        " ".repeat(maxOf(state.value.longest_shot_name - 3, 1)),
                                fontSize = 20.sp
                            )
                        }
                    }
                }
            }

            Column {
                Row {
                    Text(" Success ", fontSize = 20.sp)
                    Text("Green  ", fontSize = 20.sp)
                    Text("Penalty ", fontSize = 20.sp)
                    Text("Reset", fontSize = 20.sp)
                }

                LazyColumn() {
                    itemsIndexed(state.value.uniqueshotsname) { index, item ->
                        if (item != "Putt") {
                            StatItem(
                                state.value.uniqueshotsname[index],
                                state.value.success[index],
                                state.value.successTry[index],
                                state.value.green[index],
                                state.value.greenTry[index],
                                state.value.penalty[index],
                                state.value.penaltyTry[index],
                                state.value.reset[index],
                                state.value.resetTry[index],
                                state.value.longest_shot_name
                            )
                        }
                    }
                }
            }
        }
        if (state.value.putts[0] != 0 || state.value.putts[1] != 0 || state.value.putts[2] != 0 || state.value.putts[3] != 0 || state.value.putts[4] != 0) {
            PuttStat(
                one = state.value.putts[0],
                two = state.value.putts[1],
                three = state.value.putts[2],
                four = state.value.putts[3],
                five = state.value.putts[4]
            )
        }
        Spacer(Modifier.height(40.dp))
        Button(onClick = {
            navController.popBackStack(route = Screens.Sessions.name, inclusive = false)
        }) {
            Icon(Icons.Default.ArrowBack, "return")
        }
    }
}

@Composable
fun StatItem(shot: String, success: Int, successTry: Int, green: Int, greenTry: Int,
             penalty: Int, penaltyTry: Int, reset: Int, resetTry: Int, longest: Int) {
    Text("    ${success.toString()} / ${successTry.toString()}     ${green.toString()} / " +
            "${greenTry.toString()}   ${penalty.toString()} / ${penaltyTry.toString()}    ${reset.toString()} / ${resetTry.toString()}",
        fontSize = 20.sp)
}

@Composable
fun PuttStat(one: Int, two: Int, three: Int, four: Int, five: Int) {
    Column {
        Text("Putt", fontSize = 20.sp)
        Row {
            Column {
                Text("1", fontSize = 20.sp)
                Text("${one.toString()} ", fontSize = 20.sp)
            }
            Spacer(Modifier.width(10.dp))
            Column {
                Text("2", fontSize = 20.sp)
                Text("${two.toString()} ", fontSize = 20.sp)
            }
            Spacer(Modifier.width(10.dp))
            Column {
                Text("3", fontSize = 20.sp)
                Text("${three.toString()} ", fontSize = 20.sp)
            }
            Spacer(Modifier.width(10.dp))
            Column {
                Text("4", fontSize = 20.sp)
                Text("${four.toString()} ", fontSize = 20.sp)
            }
            Spacer(Modifier.width(10.dp))
            Column {
                Text("5", fontSize = 20.sp)
                Text("${five.toString()} ", fontSize = 20.sp)
            }
        }
    }
}