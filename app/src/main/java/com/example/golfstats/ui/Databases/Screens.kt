package com.example.golfstats.ui.Databases

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
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

@Composable
fun DatabasesScreen(
    state: DatabasesState,
    onEvent: (DatabasesEvent) -> Unit,
    navController: NavHostController
) {
    if(state.which_db_open == 0) {
        Column(Modifier.fillMaxSize(),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally) {
            Spacer(Modifier.height(13.dp))
            Button(onClick = { onEvent(DatabasesEvent.ClickCourses) },
                modifier = Modifier.height(160.dp).width(260.dp)) {
                Text("Courses", fontSize = 36.sp)
            }
            Spacer(Modifier.height(27.dp))
            Button(onClick = { onEvent(DatabasesEvent.ClickHoles) },
                modifier = Modifier.height(160.dp).width(260.dp)) {
                Text("Holes", fontSize = 36.sp)
            }
            Spacer(Modifier.height(27.dp))
            Button(onClick = { onEvent(DatabasesEvent.ClickShots) },
                modifier = Modifier.height(160.dp).width(260.dp)) {
                Text("Shots", fontSize = 36.sp)
            }
            Spacer(Modifier.height(27.dp))
            Button(onClick = { onEvent(DatabasesEvent.ClickSessions) },
                modifier = Modifier.height(160.dp).width(260.dp)) {
                Text("Sessions", fontSize = 36.sp)
            }
            Spacer(Modifier.height(13.dp))
            Button(onClick = {navController.navigateUp()},
                modifier = Modifier.height(80.dp).width(150.dp)) {
                Icon(Icons.Default.ArrowBack, "Return", Modifier.size(20.dp))
            }
            Spacer(Modifier.height(13.dp))
        }
    }
    Column(Modifier.fillMaxSize(),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally) {

        if(state.which_db_open > 0) {
            Spacer(Modifier.height(10.dp))
            Button(onClick = {onEvent(DatabasesEvent.Dismiss)},
                modifier = Modifier.height(80.dp).width(150.dp)) {
                Icon(Icons.Default.ArrowBack, "Return")
            }
            Spacer(Modifier.height(10.dp))
        }
        if(state.which_db_open == 1) {
            LazyColumn() {
                items(items = state.courses, key = { it.id }) { item ->
                    Row() {
                        Text("id=${item.id} nom=${item.nom} nb holes=${item.holes}")
                    }
                }
            }
        }
        if(state.which_db_open == 2) {
            LazyColumn() {
                items(items = state.holes, key = { it.id }) { item ->
                    Row() {
                        Text("id=${item.id} course_id=${item.course_id} numero=${item.numero} par=${item.par} yards=${item.yards}")
                    }
                }
            }
        }
        if(state.which_db_open == 3) {
            LazyColumn() {
                items(items = state.shots, key = { it.id }) { item ->
                    Row() {
                        Text("id=${item.id} session_id=${item.session_id}  hole=${item.num_hole} shot=${item.shot} success=${item.success} on_green=${item.green}")
                    }
                }
            }
        }
        if(state.which_db_open == 4) {
            LazyColumn() {
                items(items = state.sessions, key = { it.id }) { item ->
                    Row() {
                        Text("id=${item.id} course_id=${item.course_id} date=${item.date} type=${item.type}")
                    }
                }
            }
        }
    }

}