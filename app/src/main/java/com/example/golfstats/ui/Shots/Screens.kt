package com.example.golfstats.ui.Shots

import android.util.Log
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.grid.GridCells
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
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FilledTonalButton
import androidx.compose.material3.Icon
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.State
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import com.example.golfstats.Screens
import com.example.golfstats.data.Shots.ShotRow
import com.example.golfstats.data.ShotsAvailable.ShotsAvailableRow
import com.example.golfstats.ui.check_int
import com.example.golfstats.ui.check_string_to_int


@Composable
fun ShotSessionScreen(
    state: State<ShotState>,
    newShotAvailable: ShotsAvailableRow,
    error_gen: Boolean,
    onEvent: (ShotEvent) -> Unit,
    navController: NavHostController
) {

    if(state.value.is_add_shot_screen_open) {

        Column(
            Modifier.fillMaxSize(),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Row(Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.Center) {
                Button(
                    onClick = {
                        onEvent(ShotEvent.onAddShot)
                    },
                    Modifier.width(120.dp)
                ) {
                    Icon(Icons.Default.Add, "Add Shot")
                }
                Spacer(modifier = Modifier.width(30.dp))
                Button(
                    onClick = {
                        navController.popBackStack(route = Screens.Sessions.name, inclusive = false)
                        onEvent(ShotEvent.DismissShot)
                    },
                    Modifier.width(120.dp)
                ) {
                    Icon(Icons.Default.Done, "Finish")
                }
            }
            Spacer(modifier = Modifier.height(20.dp))
            RecentShots(state, onEvent)
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
fun RecentShots(state: State<ShotState>, onEvent: (ShotEvent) -> Unit, modifier: Modifier = Modifier
) {
    LazyColumn(modifier = modifier) {
        items(items = state.value.recentShotsList, key = {it.id}) {item ->
            if(item.session_id == state.value.session_id) {
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
        Row {
            Text(" " + shot_row.success.toString() + " putt")
            Button(onClick = {
                onEvent(ShotEvent.DeleteRecordedShot(shot_row))
            }) {
                Icon(Icons.Default.Delete, contentDescription = "Delete")
            }
        }
        Spacer(modifier = Modifier.height(16.dp))
    } else {
        Row {
            Text(text = shot_row.shot)
            Column {
                Text(text = " success : " + shot_row.success.toString() + " green : " + shot_row.green.toString())
                Text(text = " penalty : " + shot_row.penalty.toString() + " reset : " + shot_row.reset.toString())
            }
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
        Column {
            Text("Choix baton", fontSize=20.sp)
            if(state.value.is_delete_option) {
                LazyVerticalGrid(columns = GridCells.Fixed(3),
                    contentPadding = PaddingValues(12.dp)
                    ) {
                    items(items = state.value.shotavailableList, key = {it.shot}) {item ->
                        Button(onClick = {
                            onEvent(ShotEvent.onEditExistingShotAvailable(item))
                        },
                            Modifier.height(80.dp)) {
                            Text(item.shot, fontSize=20.sp)
                            Icon(Icons.Default.Edit,  "Edit")
                        }
                    }
                }
            } else {
                LazyVerticalGrid(columns = GridCells.Fixed(3)) {
                    items(items = state.value.shotavailableList, key = {it.shot}) { item ->
                        Button(onClick = {
                            onEvent(ShotEvent.OnChooseShot(item.shot))
                        },
                            Modifier.height(80.dp)) {
                            Text(item.shot, fontSize=20.sp)
                        }
                    }
                }
                Row {
                    Button(onClick = {
                        onEvent(ShotEvent.DismissShot)
                    }) {
                        Icon(Icons.Default.ArrowBack, contentDescription = "Return")
                    }
                    Button(onClick = {
                        onEvent(ShotEvent.SETDEFAULT)
                    }) {
                        Text("Set Default", fontSize=20.sp)
                        Icon(Icons.Default.Refresh, contentDescription = "Set Default")
                    }
                    Button(onClick = {
                        onEvent(ShotEvent.OnEditShotAvailable)
                    }) {
                        Icon(Icons.Default.Edit, contentDescription = "Edit")
                    }
                    Button(onClick = {
                        onEvent(ShotEvent.OnAddNewShotAvailable)
                    }) {
                        Icon(Icons.Default.Add, contentDescription = "Add")
                    }
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
                    //TODO mettre rouge
                    onEvent(ShotEvent.DeleteShotAvailable(newShotAvailable))
                }) {
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
                    .width(150.dp)
            )
            Row {
                OutlinedTextField(
                    value = check_int(newShotAvailable.id),
                    onValueChange = {
                        onEvent(ShotEvent.onChangeShotAvailableID(check_string_to_int(it)))
                    },
                    label = {Text("ID")},
                    modifier = Modifier
                        .width(150.dp)
                )
            }
            Row {
                Text(text="Can it be off the green and a success?")
            }
            Row {
                if(newShotAvailable.green) {
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
                } else {
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
                }

            }
            Row {
                Text(text="Can it get a penalty?")
            }
            Row {
                if(newShotAvailable.penalty) {
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
                } else {
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
                }
            }
            Row {
                Text(text="Can it get be a reset shot?")
            }
            Row {
                if(newShotAvailable.reset) {
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
                } else {
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
                }
            }
        }
        LowerBar(onEvent, error_gen)
    }
}


@Composable
fun PuttScreen(onEvent: (ShotEvent) -> Unit) {
    LazyColumn() {
        items(items = listOf(1, 2, 3, 4, 5), key = {it}) {n ->
            Button(onClick = {
                onEvent(ShotEvent.OnChangedputt(n))
            }) {
                Text(n.toString())
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
                    .width(300.dp)
            )
        }
        if (state.value.is_reset_open) {
            CheckBody(
                nom = "Had to replay the shot (< min(75% of yards, 100y))?",
                onEvent, ShotEvent::OnChangedreset,
                Modifier
                    .height(180.dp)
                    .width(300.dp)
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
fun CheckBody(nom: String, onEvent: (ShotEvent) -> Unit, event: (Int) -> ShotEvent, modifier: Modifier) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Spacer(modifier = Modifier.height(20.dp))
        Text("${nom}", fontSize = 20.sp, fontWeight = FontWeight.Bold)
        Spacer(modifier = Modifier.height(20.dp))
        Button(
            onClick = {onEvent(event(2))},
            modifier = modifier
        ) {
            Icon(Icons.Default.Done, "Yes",
                modifier = Modifier.size(40.dp))
        }
        Spacer(modifier = Modifier.height(20.dp))
        Button(onClick = {
            onEvent(event(1))
        },
            modifier = modifier) {
            Text(text="?", fontSize=40.sp)
            //TODO ajouter l'icône téléchargé du point d'interrogation. question_mark-24
            //val imageView: ImageView = findViewById(R.id.custom_icon)
            //imageView.setImageResource(R.drawable.ic_custom_icon)
        }
        Spacer(modifier = Modifier.height(20.dp))
        Button(onClick = {
            onEvent(event(0))
        },
            modifier = modifier) {
            Icon(Icons.Default.Close, "No",
                modifier = Modifier.size(60.dp))
        }
    }
}
