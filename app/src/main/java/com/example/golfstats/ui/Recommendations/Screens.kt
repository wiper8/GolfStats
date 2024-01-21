package com.example.golfstats.ui.Recommendations


import android.util.Log
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyHorizontalGrid
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import com.example.golfstats.Screens
import com.example.golfstats.data.Recommendations.RecommendationRow
import com.example.golfstats.data.ShotsAvailable.ShotsAvailableRow
import java.lang.Math.floor
import java.lang.Math.round


fun double_to_int(d: Double) : String {
    if(d < 0) return "0"
    return floor(d).toInt().toString()
}

fun double_to_float(d: Double) : String {
    if(d < 0.0) return "" //-0.1 -> ""
    if((d-floor(d)) < 0.099) return ""
    //round(d-floor(d), 1) + d
    //if((d-floor(d))*10 < 1) return "" //9.1 -> 9.07-9 -> 0.7 -> ""
    //if(floor((d-floor(d))*10).toInt() % 10 == 0) return (floor((d-floor(d))*100)/10).toInt().toString() //9.407 - 9 = 0.407  40.7 -> 4
    return round((d-floor(d))*10).toInt().toString()
}

fun is_empty_0(s: String) : String {
    if(s == "") return "0"
    return s
}


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun RecommendationsCreationScreen(
    state: RecommendationsState,
    newExpect: RecommendationRow,
    newRecommendations: List<RecommendationRow>,
    onEvent: (RecommendationsEvent) -> Unit,
    navController: NavHostController
) {
    if(state.is_choix_open) {
        Spacer(Modifier.height(20.dp))
        Column(horizontalAlignment = Alignment.CenterHorizontally) {
            Text("Choix baton", fontSize=20.sp)
            Spacer(Modifier.height(10.dp))
            LazyVerticalGrid(columns = GridCells.Fixed(3),
                verticalArrangement = Arrangement.Center) {
                items(items = state.allShotsAvailable, key = {it.shot}) { item ->
                    Column(horizontalAlignment = Alignment.CenterHorizontally) {
                        Button(onClick = {
                            onEvent(RecommendationsEvent.OnClickShot(item))
                        },
                            Modifier
                                .height(80.dp)
                                .width(120.dp)) {
                            Text(item.shot, fontSize=20.sp)
                        }
                        Spacer(Modifier.height(10.dp))
                    }
                }
            }
            Spacer(Modifier.height(20.dp))
            Row(
                horizontalArrangement = Arrangement.Center,
                verticalAlignment = Alignment.CenterVertically
            ) {
                OutlinedTextField(
                    value = double_to_int(newExpect.expectency),
                    onValueChange = {
                        onEvent(RecommendationsEvent.OnChangeExpectInt(is_empty_0(it)))
                    },
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Phone),
                    label = { Text("Expectency")},
                    modifier = Modifier
                        .width(110.dp)
                        .height(80.dp)
                )
                Text(".")
                OutlinedTextField(
                    value = double_to_float(newExpect.expectency),
                    onValueChange = {
                        onEvent(RecommendationsEvent.OnChangeExpectFloat(it))
                    },
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Phone),
                    label = { Text("Float")},
                    modifier = Modifier
                        .width(110.dp)
                        .height(80.dp)
                )
            }
            Spacer(Modifier.height(20.dp))
            Row {
                Button(onClick = {onEvent(RecommendationsEvent.DismissRecommend)},
                    Modifier
                        .height(80.dp)
                        .width(120.dp)) {
                    Icon(Icons.Default.Close, contentDescription = "Dismiss", Modifier.size(30.dp))
                }
                Button(onClick = {
                    onEvent(RecommendationsEvent.SaveRecommendation)
                },
                    Modifier
                        .height(80.dp)
                        .width(120.dp)) {
                    Text("Save", fontSize=20.sp)
                }
            }
            Spacer(Modifier.height(20.dp))
            LazyHorizontalGrid(rows = GridCells.Fixed(1)) {
                items(items = newRecommendations) { item ->
                    for (shot in state.allShotsAvailable) {
                        if(shot.id == item.shot_id)
                            Text("${shot.shot}, ", fontSize=20.sp)
                    }
                    Spacer(Modifier.height(10.dp))
                }
            }
            Spacer(Modifier.height(20.dp))

        }
    } else {
        Column(
            Modifier.fillMaxSize(),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally) {
            RecommendationsScreen(state.allShotsAvailable, recomm_ids = state.allRecommendationsID, recomm_list = state.allRecommendations, hole_id = state.hole_id, onEvent = onEvent)
            Row(Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.Center,
                verticalAlignment = Alignment.CenterVertically) {
                Button(onClick = {navController.popBackStack(route = Screens.Sessions.name, inclusive = false)}) {
                    Icon(Icons.Default.ArrowBack, "return", Modifier.size(30.dp))
                }
                Button(onClick = {onEvent(RecommendationsEvent.OnAddNewClick)}) {
                    Icon(Icons.Default.Add, "Add", Modifier.size(30.dp))
                }
            }
        }
    }
}



@Composable
fun RecommendationsScreen(allShotsAvailable: List<ShotsAvailableRow>, recomm_ids: List<Int>, recomm_list: List<RecommendationRow>, hole_id: Int?, onEvent: (RecommendationsEvent) -> Unit, modifier: Modifier = Modifier
) {
    val sortedRecommends = recomm_list
        .filter {it.hole_id == hole_id}
        .sortedBy { it.expectency }

    LazyColumn(modifier = modifier) {
        items(items = sortedRecommends.map(RecommendationRow::recommend_id).distinct(), key = {it}) {id ->
            RecommendationItem(allShotsAvailable, sortedRecommends, hole_id, id, onEvent)
        }
    }
}


@Composable
private fun RecommendationItem(
    allShotsAvailable: List<ShotsAvailableRow>, recomm_list: List<RecommendationRow>, hole_id: Int?, id: Int, onEvent: (RecommendationsEvent) -> Unit
) {
    Row {
        var expec: Double = 0.0
        for (item in recomm_list) {
            if (item.recommend_id == id && item.hole_id == hole_id) {
                expec = item.expectency
                break
            }
        }

        val filtered = recomm_list
            .filter {it.recommend_id == id}

        Text("${expec} : ")
        Button(onClick = {
            onEvent(RecommendationsEvent.DeleteRecommend(filtered))}) {
            Icon(Icons.Default.Delete, "Delete")
        }
        LazyRow(modifier = Modifier) {
            items(items = filtered, key = { it.id }) { item ->
                for (shot in allShotsAvailable) {
                    if(shot.id == item.shot_id)
                        Text("${shot.shot}, ", fontSize=20.sp)
                }
            }
        }
    }
}

@Composable
fun RecommendationsScreenShots(allShotsAvailable: List<ShotsAvailableRow>, recomm_ids: List<Int>, recomm_list: List<RecommendationRow>, hole_id: Int?, modifier: Modifier = Modifier
) {
    val sortedRecommends = recomm_list
        .filter {it.hole_id == hole_id}
        .sortedBy { it.expectency }

    LazyColumn(modifier = modifier
        .height(200.dp)
        .fillMaxHeight()) {
        items(items = sortedRecommends.map(RecommendationRow::recommend_id).distinct(), key = {it}) {id ->
            RecommendationItemShots(allShotsAvailable, sortedRecommends, hole_id, id)
        }
    }
}


@Composable
private fun RecommendationItemShots(
    allShotsAvailable: List<ShotsAvailableRow>, recomm_list: List<RecommendationRow>, hole_id: Int?, id: Int
) {
    Row {
        var expec: Double = 0.0
        for (item in recomm_list) {
            if (item.recommend_id == id && item.hole_id == hole_id) {
                expec = item.expectency
                break
            }
        }

        val filtered = recomm_list
            .filter {it.recommend_id == id}

        Spacer(Modifier.width(10.dp))
        Text("${expec} : ")
        LazyRow(modifier = Modifier) {
            items(items = filtered, key = { it.id }) { item ->
                for (shot in allShotsAvailable) {
                    if(shot.id == item.shot_id)
                        Text("${shot.shot}, ", fontSize=20.sp)
                }
            }
        }
    }
}