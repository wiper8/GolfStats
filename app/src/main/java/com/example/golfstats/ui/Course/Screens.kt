package com.example.golfstats.ui.Course

import android.util.Log
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.defaultMinSize
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyHorizontalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.lazy.grid.itemsIndexed
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.text.selection.LocalTextSelectionColors
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Create
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Done
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FilledTonalButton
import androidx.compose.material3.Icon
import androidx.compose.material3.LocalTextStyle
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextFieldColors
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.graphics.takeOrElse
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import com.example.golfstats.Screens
import com.example.golfstats.data.Course.CourseRow
import com.example.golfstats.data.Holes.HoleRow
import com.example.golfstats.data.Sessions.SessionRow
import com.example.golfstats.ui.Sessions.SessionEvent
import com.example.golfstats.ui.Sessions.SessionsState
import com.example.golfstats.ui.Shots.ShotEvent
import com.example.golfstats.ui.check_int
import com.example.golfstats.ui.check_string_to_int


@Composable
fun AvailableCourses(
    coursesList: List<CourseRow>, existingSessions: List<SessionRow>, onEvent: (SessionEvent) -> Unit,
    session_date: String
) {
    LazyColumn() {
        itemsIndexed(items = coursesList) { index, item ->
            CourseItem(row = item, existingSessions, onEvent, session_date)
        }
    }
}

@Composable
fun CourseItem(
    row: CourseRow, existingSessions: List<SessionRow>, onEvent: (SessionEvent) -> Unit,
    session_date: String, modifier: Modifier = Modifier,
) {
    Row(
        modifier = Modifier.fillMaxWidth()
    ) {
        Text(
            text = row.nom,
            style = MaterialTheme.typography.titleLarge,
        )
        Spacer(Modifier.weight(1f))
        Text(text = row.holes.toString())
        var ok = true
        for(session_row in existingSessions) {
            ok = ok && session_date != session_row.date
            if(!ok) break
        }
        if(session_date != "" && session_date != "range" && ok) {
            Button(
                onClick = {
                    onEvent(SessionEvent.SetSessionTypeCourse)
                    onEvent(SessionEvent.PlayCourse(row.id))
                    onEvent(SessionEvent.SaveSession)
                },
                modifier = Modifier.width(75.dp).height(60.dp)
            ) {
                Icon(Icons.Default.PlayArrow, contentDescription = "Play")
            }
        } else {
            FilledTonalButton(onClick = {},
                modifier = Modifier.width(75.dp).height(60.dp)) {
                Icon(Icons.Default.PlayArrow, contentDescription = "Play")
            }
        }

        //TODO ajouter le bouton pour modifier un parcours ainsi que les ajustements dans les BD
        //Button(onClick = {
        //    onEvent(SessionEvent.EditCourse(row))
        //}) {
        //    Icon(Icons.Default.Create, contentDescription = "Edit")
        //}
        Button(onClick = {
            onEvent(SessionEvent.DeleteCourse(row))
        }, modifier =  Modifier.width(75.dp).height(60.dp)) {
            Icon(Icons.Default.Delete, contentDescription = "Delete")
        }
    }
}

@Composable
fun CourseItemRange(
    state: SessionsState, session_row: SessionRow, existingSessions: List<SessionRow>, onEvent: (SessionEvent) -> Unit,
    session_date: String, onNavClick: (Int, Int, Int) -> Unit, modifier: Modifier = Modifier,
) {
    if(state.is_session_id_found) {
        onEvent(SessionEvent.OffSessionIdFound)
        onNavClick(state.session_id, -1, 0)
    }
    Row(
        modifier = Modifier.fillMaxWidth()
    ) {
        Text(
            text = "Range",
            style = MaterialTheme.typography.titleLarge,
        )
        Spacer(Modifier.weight(1f))
        var ok = true
        for(session_row in existingSessions) {
            ok = ok && session_date != session_row.date
            if(!ok) break
        }

        if(session_date != "" && ok) {
            Button(
                onClick = {
                    onEvent(SessionEvent.PlayCourse(-1)) //TODO remove?
                    onEvent(SessionEvent.SaveSessionRange)
                },
                modifier = Modifier
                    .width(75.dp)
                    .height(60.dp)
            ) {
                Icon(Icons.Default.PlayArrow, contentDescription = "Play")
            }
        } else {
            FilledTonalButton(onClick = {},
                modifier = Modifier
                    .width(75.dp)
                    .height(60.dp)) {
                Icon(Icons.Default.PlayArrow, contentDescription = "Play")
            }
        }

        FilledTonalButton(onClick = {},
            modifier = Modifier
                .width(75.dp)
                .height(60.dp)) {
            Icon(Icons.Default.Delete, contentDescription = "Delete")
        }
    }
}


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun NewCourseRowScreen(
    holes_list: List<HoleRow>,
    newRow: CourseRow,
    onEvent: (SessionEvent) -> Unit
) {

    Row (
        modifier = Modifier
            .fillMaxWidth()
            .height(580.dp),
        horizontalArrangement = Arrangement.Center,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Column {
            OutlinedTextField(
                value = newRow.nom,
                onValueChange = {
                    onEvent(SessionEvent.OnChangednomCourse(it))
                },
                label = { Text("Nom")},
                modifier = Modifier
                    .width(110.dp)
                    .height(80.dp)
            )
            Button(onClick = {
                onEvent(SessionEvent.OnChangeNumHoles(9))
            },
                Modifier
                    .width(70.dp)
                    .height(70.dp)) {
                Text("9")
            }

            Button(onClick = {
                onEvent(SessionEvent.OnChangeNumHoles(18))
            },
                Modifier
                    .width(70.dp)
                    .height(70.dp)) {
                Text("18")
            }

            Button(
                onClick = {
                    onEvent(SessionEvent.DismissCourse)
                },
                Modifier
                    .width(70.dp)
                    .height(70.dp)
            ) {
                Icon(Icons.Default.Close, "Dismiss", Modifier.size(60.dp))
            }

            if(newRow.nom != "") {
                Button(
                    onClick = {onEvent(SessionEvent.SaveCourse)},
                    modifier = Modifier.width(70.dp).height(70.dp)
                ) {
                    Icon(Icons.Default.Done, "Save", Modifier.size(60.dp))
                }
            } else {
                FilledTonalButton(onClick = {},
                    modifier = Modifier.width(70.dp).height(70.dp)) {
                    Icon(Icons.Default.Done, "Save", Modifier.size(60.dp))
                }
            }
        }
        Column {
            LazyHorizontalGrid(
                rows = GridCells.Fixed(9),
                verticalArrangement = Arrangement.Center,
                horizontalArrangement = Arrangement.Center
            ) {
                items(items = (1..newRow.holes).toList()) {
                    Column(horizontalAlignment = Alignment.CenterHorizontally) {
                        Row {
                            Text(it.toString(), fontSize=20.sp)
                            OutlinedTextField(
                                value = check_int(holes_list.get(it-1).par),
                                onValueChange = {str ->
                                    onEvent(SessionEvent.OnChangePar(it, check_string_to_int(str)))
                                },
                                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Phone),
                                label = { Text("Par", fontSize = 10.sp) },
                                modifier = Modifier
                                    .width(50.dp)
                                    .height(70.dp)
                            )
                            OutlinedTextField(
                                value = check_int(holes_list.get(it-1).yards),
                                onValueChange = {str ->
                                    onEvent(SessionEvent.OnChangeYards(it, check_string_to_int(str)))
                                },
                                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Phone),
                                label = { Text("Yards", fontSize = 10.sp) },
                                modifier = Modifier
                                    .width(65.dp)
                                    .height(70.dp)
                            )
                            Spacer(modifier = Modifier.width(5.dp))
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun CourseScoreCard(
    state: SessionsState,
    onEvent: (SessionEvent) -> Unit,
    onNavClick: (Int, Int, Int) -> Unit,
    navController: NavHostController
) {
    if(state.is_settings_open) {
        SettingsScreen(onEvent)
    } else {
        Column {
            Row {
                if (state.is_score_total_visible) {
                    Text("Total : ${state.total_shots} (${if(state.total_shots_under_par>0) "+" else ""}${state.total_shots_under_par})")
                } else {
                    Text("Total : ? (?)")
                }
                Spacer(modifier = Modifier.width(30.dp))
                Button(onClick = {
                    onEvent(SessionEvent.ExitCard)
                    navController.navigateUp()
                }) {
                    Icon(Icons.Default.ArrowBack, "return", Modifier.size(30.dp))
                }
                Spacer(modifier = Modifier.width(70.dp))
                Button(onClick = { onEvent(SessionEvent.Settings) }) {
                    Icon(Icons.Default.Settings, "Settings", Modifier.size(40.dp))
                }
            }

            /*
            LazyHorizontalGrid(
                rows = GridCells.Fixed(1),
                verticalArrangement = Arrangement.Center,
                horizontalArrangement = Arrangement.Center
            ) {
                items(items = List(2) { it + 1 }) {
                    Row {
                        Text("Trou")
                        Text("Coups")
                        Text("Par")
                        Text("Yards")
                    }
                }
            }
            */

            onEvent(SessionEvent.CalculateScores(state.session_id))

            if (state.are_shots_calculated) {
                LazyHorizontalGrid(
                    rows = GridCells.Fixed(9),
                    horizontalArrangement = Arrangement.Center
                ) {
                    itemsIndexed(items = state.holesCurr_Course) { i, it ->
                        Column(horizontalAlignment = Alignment.CenterHorizontally) {
                            Row(
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Button(
                                    onClick = {
                                        onNavClick(state.session_id, it.course_id, it.numero)
                                    },
                                    Modifier
                                        .width(60.dp)
                                        .height(60.dp)
                                ) {
                                    Text(it.numero.toString(), fontSize = 20.sp)
                                }
                                Text("${state.scores_holes[i]}") // nb de coups
                                Text(check_int(it.par)) // Par
                                Text(check_int(it.yards)) // Yards
                                Spacer(modifier = Modifier.width(5.dp))
                            }
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun SettingsScreen(
    onEvent: (SessionEvent) -> Unit
) {
    Column {
        Text("Show total score and progress?")
        Row {
            Button(onClick = {
                onEvent(SessionEvent.SetSettings(true))
            }) {
                Text("Yes")
            }
            Button(onClick = {
                onEvent(SessionEvent.SetSettings(false))
            }) {
                Text("No")
            }
        }
    }
}


