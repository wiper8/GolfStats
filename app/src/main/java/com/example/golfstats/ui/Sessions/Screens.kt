@file:OptIn(ExperimentalMaterial3Api::class)

package com.example.golfstats.ui.Sessions

import android.util.Log
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Create
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Done
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FilledTonalButton
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.State
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import com.example.golfstats.Screens
import com.example.golfstats.data.Course.CourseRow
import com.example.golfstats.data.Holes.HoleRow
import com.example.golfstats.data.Sessions.SessionRow
import com.example.golfstats.ui.Course.AskRecommendationsScreen
import com.example.golfstats.ui.Course.AvailableCourses
import com.example.golfstats.ui.Course.CourseItem
import com.example.golfstats.ui.Course.CourseItemRange
import com.example.golfstats.ui.Course.CourseScoreCard
import com.example.golfstats.ui.Course.NewCourseRowScreen
import com.example.golfstats.ui.Shots.ShotEvent
import com.example.golfstats.ui.Stats.StatEvent
import com.example.golfstats.ui.Stats.StatsState

@Composable
fun SessionsScreen(
    state: SessionsState,
    onEvent: (SessionEvent) -> Unit,
    newRow: SessionRow,
    newCourseHoles: List<HoleRow>,
    newCourseRow: CourseRow,
    navController: NavHostController,
    onNavClick: (Int, Int, Int, Int) -> Unit,
    onNavClickRecomm: (Int, Int) -> Unit,
    range_only: Boolean = true
) {

    if(range_only) {
        if(state.is_session_id_found) {
            Column(
                modifier = Modifier.fillMaxHeight().fillMaxWidth(),
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.CenterHorizontally) {
                Button(onClick = { onNavClick(state.session_id, -1, 0, -1) }) {
                    Text("Go to range", fontSize = 40.sp)
                }
                Spacer(Modifier.height(10.dp))
                Button(onClick = {onEvent(SessionEvent.ExitRangeScreen)}) {
                    Icon(Icons.Default.ArrowBack, "return", Modifier.size(40.dp))
                }
            }
        }
    }

    if(state.is_new_screen_open){
        NewSessionRowScreen(newRow = newRow, existingSessions = state.sessionsList, onEvent = onEvent, range_only = range_only, state = state, onNavClick = onNavClick)
    } else if(state.is_new_course_screen_open){
        if(state.is_add_recommendations_screen_open) {
            AskRecommendationsScreen(holes_list = newCourseHoles, onEvent, onNavClickRecomm)
        } else {
            NewCourseRowScreen(holes_list = newCourseHoles, newRow = newCourseRow, allCourses = state.allCourses, onEvent = onEvent)
        }
    } else if(state.is_card_screen_open) {
        CourseScoreCard(state = state, onEvent = onEvent, onNavClick = onNavClick, navController = navController)
    } else if(!state.is_new_screen_open && !state.is_new_course_screen_open && !state.is_card_screen_open && ((range_only && !state.is_session_id_found) || !range_only)) {

        Column(modifier = Modifier
            .fillMaxHeight()
            .fillMaxWidth(),
            horizontalAlignment = Alignment.CenterHorizontally) {
            SessionsList(state, onEvent = onEvent, range_only = range_only, onNavClick = onNavClick)
            Row {
                Spacer(Modifier.width(10.dp))
                Button(onClick = {
                    navController.popBackStack(route = Screens.Menu.name, inclusive = false)
                }) {
                    Icon(Icons.Default.ArrowBack, "return")
                }
                Button(onClick = {
                    onEvent(SessionEvent.OnAddNewClick)
                }) {
                    Icon(Icons.Default.Add, contentDescription = "Add")
                }
            }
        }
    }
}

@Composable
fun StatSessionsScreen(
    state: SessionsState,
    onEvent: (SessionEvent) -> Unit,
    navController: NavHostController,
    onNavClick: (Int) -> Unit
) {


    Column {
        StatSessionsList(sessionsList = state.sessionsList,
            onEvent = onEvent, onNavClick = onNavClick)
        Row {
            Spacer(Modifier.width(10.dp))
            Button(onClick = {
                navController.popBackStack(route = Screens.Menu.name, inclusive = false)
            }) {
                Icon(Icons.Default.ArrowBack, "return", modifier = Modifier.size(20.dp))
            }
            Button(onClick = {
                onNavClick(-1)
            }) {
                Text("All stats", fontSize = 20.sp)
            }
        }
    }
}

@Composable
private fun SessionsList(
    state: SessionsState, onEvent: (SessionEvent) -> Unit, range_only: Boolean, onNavClick: (Int, Int, Int, Int) -> Unit,
    modifier: Modifier = Modifier
) {
    LazyColumn(modifier = Modifier
        .fillMaxWidth(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
        ) {
        items(items = state.sessionsList, key = { it.id }) { item ->
            if(range_only) {
                if(item.type == "range") {
                    SessionItem(
                        state = state, row = item, onEvent = onEvent, range_only = range_only,
                        onNavClick = onNavClick
                    )
                }
            } else {
                if(item.type == "course") {
                    SessionItem(state = state, row = item, onEvent = onEvent, range_only = range_only,
                        onNavClick = onNavClick)
                }
            }
        }
    }
}

@Composable
private fun SessionItem(
    state: SessionsState, row: SessionRow, onEvent: (SessionEvent) -> Unit, range_only: Boolean, onNavClick: (Int, Int, Int, Int) -> Unit, modifier: Modifier = Modifier
) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Spacer(Modifier.width(10.dp))
        Text(
            text = row.date,
            style = MaterialTheme.typography.titleLarge,
        )
        Spacer(Modifier.weight(1f))
        Button(onClick = {
            if(range_only) {
                onEvent(SessionEvent.ResumeSessionRange(row))
            } else {
                onEvent(SessionEvent.SetSessionTypeCourse)
                onEvent(SessionEvent.ResumeSession(row))
            }

        }) {
            Icon(Icons.Default.PlayArrow, contentDescription = "Play")
        }/*
        Button(onClick = {
            onEvent(SessionEvent.Edit(row))
        }) {
            Icon(Icons.Default.Create, contentDescription = "Edit")
        }*/
        Button(onClick = {
            onEvent(SessionEvent.Delete(row))
        }) {
            Icon(Icons.Default.Delete, contentDescription = "Delete")
        }
    }
}


@Composable
private fun StatSessionsList(
    sessionsList: List<SessionRow>, onEvent: (SessionEvent) -> Unit,
    onNavClick: (Int) -> Unit, modifier: Modifier = Modifier
) {
    LazyColumn(modifier = modifier) {
        items(items = sessionsList, key = { it.id }) { item ->
            StatSessionItem(row = item, onNavClick = onNavClick)
        }
    }
}

@Composable
private fun StatSessionItem(
    row: SessionRow,
    onNavClick: (Int) -> Unit, modifier: Modifier = Modifier
) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Spacer(Modifier.width(10.dp))
        Text(
            text = row.date,
            style = MaterialTheme.typography.titleLarge,
        )
        Spacer(Modifier.weight(1f))
        Button(onClick = {
            onNavClick(row.id)
        }) {
            Text("Stats", fontSize = 20.sp)
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun NewSessionRowScreen(
    newRow: SessionRow,
    existingSessions: List<SessionRow>,
    onEvent: (SessionEvent) -> Unit,
    range_only: Boolean = true,
    state: SessionsState,
    onNavClick: (Int, Int, Int, Int) -> Unit,) {
    Column(modifier = Modifier
        .fillMaxWidth()
        .fillMaxHeight()) {
        Row(modifier = Modifier
            .fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically) {
            Spacer(Modifier.width(10.dp))
            OutlinedTextField(
                value = newRow.date,
                onValueChange = {
                    onEvent(SessionEvent.OnChangeddate(it))
                },
                label = { Text("Date") },
                modifier = Modifier
                    .width(130.dp),
                colors = TextFieldDefaults.outlinedTextFieldColors(
                    textColor = Color.DarkGray
                ))
            Spacer(Modifier.weight(1f))
            Button(onClick = {
                onEvent(SessionEvent.Dismiss)
            }) {
                Icon(Icons.Default.ArrowBack, contentDescription = "Dismiss")
            }
            Spacer(Modifier.weight(1f))
            if(range_only) {
                CourseItemRange(state, newRow, existingSessions, onEvent, newRow.date, onNavClick)
            } else {
                Button(onClick = {
                    onEvent(SessionEvent.OnAddNewCourseClick)
                }, ) {
                    Text("Add New Course")
                }
            }
            Spacer(Modifier.width(10.dp))
        }
        if(!range_only) AvailableCourses(state.allCourses, existingSessions, onEvent, newRow)
    }
}