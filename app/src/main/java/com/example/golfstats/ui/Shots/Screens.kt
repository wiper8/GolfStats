package com.example.golfstats.ui.Shots

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
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyHorizontalGrid
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Done
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonColors
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FilledTonalButton
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.State
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import com.example.golfstats.Screens
import com.example.golfstats.data.Recommendations.RecommendationRow
import com.example.golfstats.data.Shots.ShotRow
import com.example.golfstats.data.ShotsAvailable.ShotsAvailableRow
import com.example.golfstats.ui.Recommendations.RecommendationsScreen
import com.example.golfstats.ui.Recommendations.RecommendationsScreenShots
import com.example.golfstats.ui.check_int
import com.example.golfstats.ui.check_string_to_int


@Composable
fun ShotSessionScreen(
    state: State<ShotState>,
    newShotAvailable: ShotsAvailableRow,
    error_gen: Boolean,
    onEvent: (ShotEvent) -> Unit,
    navController: NavHostController,
    range_only: Boolean
) {

    if(state.value.is_add_shot_screen_open) {

        Column(
            Modifier.fillMaxSize(),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {

            Spacer(modifier = Modifier.height(20.dp))
            Row(Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.Center,
                verticalAlignment = Alignment.CenterVertically) {
                Spacer(Modifier.height(10.dp))
                Button(
                    onClick = {
                        if(state.value.course_id == -1) {
                            navController.popBackStack(route = Screens.Menu.name, inclusive = false)
                        } else {
                            navController.navigateUp()
                        }
                        onEvent(ShotEvent.DismissShot)
                    },
                    Modifier
                        .width(120.dp)
                        .height(120.dp)
                ) {
                    Icon(Icons.Default.Done, "Finish", Modifier.size(50.dp))
                }
                Spacer(modifier = Modifier.width(30.dp))
                Button(
                    onClick = {
                        onEvent(ShotEvent.onAddShot)
                    },
                    Modifier
                        .width(120.dp)
                        .height(120.dp)
                ) {
                    Icon(Icons.Default.Add, "Add Shot", Modifier.size(50.dp))
                }
            }
            Spacer(modifier = Modifier.height(20.dp))
            if(range_only) {
                RecentShots(state, onEvent, 700.dp)
            } else {
                RecentShots(state, onEvent, 500.dp)
            }

            Spacer(modifier = Modifier.height(15.dp))
            RecommendationsScreenShots(state.value.shotavailableList, state.value.recomm_ids, state.value.recomm_list, state.value.hole_id)
        }
    }
    if(state.value.is_choix_club_open) {
        ChoixBatonScreen(state, onEvent, newShotAvailable, error_gen)
    }
    if(state.value.is_putt_open) {
        PuttScreen(onEvent)
    }
    CheckScreen(state = state, onEvent)
}



@Composable
fun RecentShots(state: State<ShotState>, onEvent: (ShotEvent) -> Unit, height: Dp = 500.dp, modifier: Modifier = Modifier
) {
    LazyColumn(modifier = modifier
        .height(height)
        .fillMaxHeight()) {
        items(items = state.value.recentShotsList, key = {it.id}) {item ->
            if(item.session_id == state.value.session_id && item.num_hole == state.value.hole_num) {
                ShotsItem(shot_row = item, onEvent = onEvent)
            }
        }
    }
}




@Composable
private fun ShotsItem(
    shot_row: ShotRow, onEvent: (ShotEvent) -> Unit, modifier: Modifier = Modifier
) {
    if(shot_row.is_putt) {
        Row(horizontalArrangement = Arrangement.Center,
            verticalAlignment = Alignment.CenterVertically) {
            Spacer(modifier = Modifier.width(10.dp))
            Text(shot_row.shot)
            Spacer(modifier = Modifier.weight(1f))
            Text(" " + shot_row.success.toString() + " putt")
            Spacer(modifier = Modifier.weight(1f))
            Button(onClick = {
                onEvent(ShotEvent.DeleteRecordedShot(shot_row))
            }) {
                Icon(Icons.Default.Delete, contentDescription = "Delete")
            }
        }
        Spacer(modifier = Modifier.height(16.dp))
    } else {
        Row(horizontalArrangement = Arrangement.Center,
            verticalAlignment = Alignment.CenterVertically) {
            Spacer(modifier = Modifier.width(10.dp))
            Text(text = shot_row.shot)
            Spacer(modifier = Modifier.weight(1f))
            Text(text = " ${if(shot_row.success == 2) "succès" else if(shot_row.success == 1) "succès?" else "échec"} ${if(shot_row.green == 2) "green" else if(shot_row.green == 1) "green?" else ""}" + " ${if(shot_row.penalty == 2) "pénalité" else if(shot_row.penalty == 1) "pénalité?" else  ""}" + " ${if(shot_row.reset == 2) "reset" else if(shot_row.reset == 1) "reset?" else ""}")
            Spacer(modifier = Modifier.weight(1f))
            /*Column {
                Text(text = " success : " + shot_row.success.toString() + " green : " + shot_row.green.toString())
                Text(text = " penalty : " + shot_row.penalty.toString() + " reset : " + shot_row.reset.toString())
            }*/
            Button(onClick = {
                onEvent(ShotEvent.DeleteRecordedShot(shot_row))
            }) {
                Icon(Icons.Default.Delete, contentDescription = "Delete")
            }
            Spacer(modifier = Modifier.height(16.dp))
        }

    }

}


@Composable
fun ChoixBatonScreen(state: State<ShotState>, onEvent: (ShotEvent) -> Unit,
                     newShotAvailable: ShotsAvailableRow, error_gen: Boolean) {
    if(!state.value.is_edit_choix_club_open) {
        Spacer(Modifier.height(20.dp))
        Column(horizontalAlignment = Alignment.CenterHorizontally) {
            Text("Choix baton", fontSize=20.sp)
            Spacer(Modifier.height(10.dp))
            if(state.value.is_delete_option) {
                LazyVerticalGrid(columns = GridCells.Fixed(3),
                    verticalArrangement = Arrangement.Center
                    ) {
                    items(items = state.value.shotavailableList, key = {it.shot}) {item ->
                        Column(horizontalAlignment = Alignment.CenterHorizontally) {
                            Button(onClick = {
                                onEvent(ShotEvent.onEditExistingShotAvailable(item))
                            },
                                Modifier
                                    .height(80.dp)
                                    .width(120.dp)) {
                                Text(item.shot, fontSize=20.sp)
                                Icon(Icons.Default.Edit,  "Edit")
                            }
                            Spacer(Modifier.height(10.dp))
                        }
                    }
                }
                Spacer(Modifier.height(20.dp))
                Row {
                    Button(onClick = {
                        onEvent(ShotEvent.DismissShotAvailableEdit)
                    },
                        Modifier.height(53.dp).width(80.dp)) {
                        Icon(Icons.Default.ArrowBack, contentDescription = "Return")
                    }
                }
            } else {
                LazyVerticalGrid(columns = GridCells.Fixed(3),
                    verticalArrangement = Arrangement.Center) {
                    items(items = state.value.shotavailableList, key = {it.shot}) { item ->
                        Column(horizontalAlignment = Alignment.CenterHorizontally) {
                            Button(onClick = {
                                onEvent(ShotEvent.OnChooseShot(item.shot))
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
                Row {
                    Spacer(Modifier.weight(1f))
                    Button(onClick = {
                        onEvent(ShotEvent.DismissShot)
                    }) {
                        Icon(Icons.Default.ArrowBack, contentDescription = "Return")
                    }
                    Spacer(Modifier.weight(1f))
                    Button(onClick = {
                        onEvent(ShotEvent.SETDEFAULT)
                    }) {
                        Text("Set Default", fontSize=20.sp)
                        Icon(Icons.Default.Refresh, contentDescription = "Set Default")
                    }
                    Spacer(Modifier.weight(1f))
                    Button(onClick = {
                        onEvent(ShotEvent.OnEditShotAvailable)
                    }) {
                        Icon(Icons.Default.Edit, contentDescription = "Edit")
                    }
                    Spacer(Modifier.weight(1f))
                    Button(onClick = {
                        onEvent(ShotEvent.OnAddNewShotAvailable)
                    }) {
                        Icon(Icons.Default.Add, contentDescription = "Add")
                    }
                    Spacer(Modifier.weight(1f))
                }
            }

        }
    } else {
        NewShotAvailable(state, onEvent, newShotAvailable, error_gen)
    }

}

@Composable
fun LowerBar(
    onEvent: (ShotEvent) -> Unit,
    error_gen: Boolean
) {
    Row {
        Button(onClick = {
            onEvent(ShotEvent.DismissShotAvailable)
            //navController.popBackStack(route = Screens.NewYardageRow.name, inclusive = true)
            //navController.navigate(Screens.Yardages.name)
        }) {
            Icon(Icons.Default.Close, contentDescription = "Dismiss")
        }
        if(error_gen) {
            FilledTonalButton(
                onClick = {
                    onEvent(ShotEvent.SaveShotAvailable)
                }
            ) {
                Icon(Icons.Default.Done, contentDescription = "Save")
            }
        } else {
            Button(
                onClick = {
                    onEvent(ShotEvent.SaveShotAvailable)
                }
            ) {
                Icon(Icons.Default.Done, contentDescription = "Save")
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun NewShotAvailable(
    state: State<ShotState>,
    onEvent: (ShotEvent) -> Unit,
    newShotAvailable: ShotsAvailableRow,
    error_gen: Boolean
) {
    Column {
        Column {
            if(state.value.is_delete_option) {
                Button(onClick = {
                    onEvent(ShotEvent.DeleteShotAvailable(newShotAvailable))
                },
                    colors = ButtonDefaults.buttonColors(
                        containerColor = Color.Red
                    )) {
                    Text("Delete")
                }
            }
            OutlinedTextField(
                value = newShotAvailable.shot,
                onValueChange = {
                    onEvent(ShotEvent.onChangeShotAvailableName(it))
                },
                label = {Text("Shot")},
                modifier = Modifier
                    .width(150.dp),
                colors = TextFieldDefaults.outlinedTextFieldColors(
                    textColor = Color.DarkGray
                )
            )
            Row {
                OutlinedTextField(
                    value = check_int(newShotAvailable.id),
                    onValueChange = {
                        onEvent(ShotEvent.onChangeShotAvailableID(check_string_to_int(it)))
                    },
                    label = {Text("ID")},
                    modifier = Modifier
                        .width(150.dp),
                    colors = TextFieldDefaults.outlinedTextFieldColors(
                        textColor = Color.DarkGray
                    )
                )
            }
            Row {
                Text(text="Can it be on the green?")
            }
            Row {
                if(newShotAvailable.green) {
                    Button(onClick = {
                        onEvent(ShotEvent.ClickButtonGreen(false))
                    }) {
                        Icon(Icons.Default.Close, "No")
                    }
                    FilledTonalButton(onClick = {
                        onEvent(ShotEvent.ClickButtonGreen(true))
                    }) {
                        Icon(Icons.Default.Done, "Yes")
                    }
                } else {
                    FilledTonalButton(onClick = {
                        onEvent(ShotEvent.ClickButtonGreen(false))
                    }) {
                        Icon(Icons.Default.Close, "No")
                    }
                    Button(onClick = {
                        onEvent(ShotEvent.ClickButtonGreen(true))
                    }) {
                        Icon(Icons.Default.Done, "Yes")
                    }
                }

            }
            Row {
                Text(text="Can it get a penalty?")
            }
            Row {
                if(newShotAvailable.penalty) {
                    Button(onClick = {
                        onEvent(ShotEvent.ClickButtonPenalty(false))
                    }) {
                        Icon(Icons.Default.Close, "No")
                    }
                    FilledTonalButton(onClick = {
                        onEvent(ShotEvent.ClickButtonPenalty(true))
                    }) {
                        Icon(Icons.Default.Done, "Yes")
                    }
                } else {
                    FilledTonalButton(onClick = {
                        onEvent(ShotEvent.ClickButtonPenalty(false))
                    }) {
                        Icon(Icons.Default.Close, "No")
                    }
                    Button(onClick = {
                        onEvent(ShotEvent.ClickButtonPenalty(true))
                    }) {
                        Icon(Icons.Default.Done, "Yes")
                    }
                }
            }
            Row {
                Text(text="Can it get be a reset shot?")
            }
            Row {
                if(newShotAvailable.reset) {
                    Button(onClick = {
                        onEvent(ShotEvent.ClickButtonReset(false))
                    }) {
                        Icon(Icons.Default.Close, "No")
                    }
                    FilledTonalButton(onClick = {
                        onEvent(ShotEvent.ClickButtonReset(true))
                    }) {
                        Icon(Icons.Default.Done, "Yes")
                    }
                } else {
                    FilledTonalButton(onClick = {
                        onEvent(ShotEvent.ClickButtonReset(false))
                    }) {
                        Icon(Icons.Default.Close, "No")
                    }
                    Button(onClick = {
                        onEvent(ShotEvent.ClickButtonReset(true))
                    }) {
                        Icon(Icons.Default.Done, "Yes")
                    }
                }
            }
        }
        LowerBar(onEvent, error_gen)
    }
}


@Composable
fun PuttScreen(onEvent: (ShotEvent) -> Unit) {
    LazyColumn(horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center,
        modifier = Modifier.fillMaxSize()) {
        items(items = listOf(1, 2, 3, 4, 5), key = {it}) {n ->
            Spacer(Modifier.height(20.dp))
            Button(onClick = {
                onEvent(ShotEvent.OnChangedputt(n))
            },
                Modifier
                    .width(130.dp)
                    .height(130.dp)) {
                Text(n.toString(), fontSize = 60.sp)
            }
        }
    }
}


@Composable
fun CheckScreen(state: State<ShotState>, onEvent: (ShotEvent) -> Unit) {
    Column(
        Modifier.fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        if (state.value.is_success_open) {
            CheckBody(
                nom = "Successful shot?",
                onEvent, ShotEvent::OnChangedsucess,
                Modifier
                    .height(180.dp)
                    .width(300.dp)
            )
        }
        if (state.value.is_green_open) {
            CheckBody(
                nom = "Is on the green?",
                onEvent, ShotEvent::OnChangedgreen,
                Modifier
                    .height(180.dp)
                    .width(300.dp)
            )
        }
        if (state.value.is_penalty_open) {
            CheckBody(
                nom = "Got a penalty?",
                onEvent, ShotEvent::OnChangedpenalty,
                Modifier
                    .height(180.dp)
                    .width(300.dp),
                good = false
            )
        }
        if (state.value.is_reset_open) {
            CheckBody(
                nom = "Had to replay the shot (< min(75% of yards, 100y))?",
                onEvent, ShotEvent::OnChangedreset,
                Modifier
                    .height(180.dp)
                    .width(300.dp),
                good = false
            )
        }
        if (state.value.is_success_open || state.value.is_green_open || state.value.is_penalty_open || state.value.is_reset_open) {
            Spacer(modifier = Modifier.height(100.dp))
            Button(onClick = {
                onEvent(ShotEvent.DismissShot)
            },
                Modifier
                    .height(180.dp)
                    .width(300.dp)) {
                Icon(Icons.Default.ArrowBack, contentDescription = "Cancel",
                    modifier = Modifier.size(40.dp))
            }
        }
    }
}

@Composable
fun CheckBody(nom: String, onEvent: (ShotEvent) -> Unit, event: (Int) -> ShotEvent, modifier: Modifier, good: Boolean = true) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Spacer(modifier = Modifier.height(20.dp))
        Text("${nom}", fontSize = 20.sp, fontWeight = FontWeight.Bold)
        Spacer(modifier = Modifier.height(20.dp))
        Button(
            onClick = {onEvent(event(2))},
            modifier = modifier,
            colors = ButtonDefaults.buttonColors(
                containerColor = if(good) Color.Green else Color.Red
            )
        ) {
            Icon(Icons.Default.Done, "Yes",
                modifier = Modifier.size(60.dp))
        }
        Spacer(modifier = Modifier.height(20.dp))
        Button(onClick = {onEvent(event(1))},
            modifier = modifier,
            colors = ButtonDefaults.buttonColors(
                containerColor = Color.Yellow
            )) {
            Text(text="?", fontSize=60.sp)
            //TODO ajouter l'icône téléchargé du point d'interrogation. question_mark-24
            //val imageView: ImageView = findViewById(R.id.custom_icon)
            //imageView.setImageResource(R.drawable.ic_custom_icon)
        }
        Spacer(modifier = Modifier.height(20.dp))
        Button(onClick = {onEvent(event(0))},
            modifier = modifier,
            colors = ButtonDefaults.buttonColors(
                containerColor = if(good) Color.Red else Color.Green
            )) {
            Icon(Icons.Default.Close, "No",
                modifier = Modifier.size(60.dp))
        }
    }
}
