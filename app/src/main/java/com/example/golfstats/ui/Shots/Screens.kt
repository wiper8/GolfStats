package com.example.golfstats.ui.Shots

import android.util.Log
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
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
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import com.example.golfstats.Screens
import com.example.golfstats.data.Shots.ShotRow
import com.example.golfstats.data.ShotsAvailable.ShotsAvailableRow
import com.example.golfstats.ui.AppViewModelProvider
import com.example.golfstats.ui.Sessions.SessionEvent
import com.example.golfstats.ui.check_int
import com.example.golfstats.ui.check_string_to_int


@Composable
fun ShotSessionScreen(
    session_id: Int,
    navController: NavHostController
) {
    val viewModel: ShotViewModel = viewModel(factory = AppViewModelProvider.Factory)
    val state = viewModel.state.collectAsState()
    val onEvent = viewModel::onEvent

    onEvent(ShotEvent.SetSessionId(session_id))

    if(state.value.is_add_shot_screen_open) {
        Column {
            Row {
                Button(onClick = {
                    onEvent(ShotEvent.onAddShot)
                }) {
                    Icon(Icons.Default.Add, "Add Shot")
                }
                Button(
                    onClick = {
                        //TODO
                    }
                ) {
                    Row {
                        //Image(imageVector = Icons.Default.Add, contentDescription = "multiple add",
                            //modifier = Modifier.clickable {
                                //isClicked = !isClicked
                                ////onClick()
                            //})
                        Text("Multiple Shots")
                    }
                }
            }
            Row {
                Button(onClick = {
                    navController.popBackStack(route = Screens.Menu.name, inclusive = false)
                    onEvent(ShotEvent.DismissShot)
                }) {
                    Icon(Icons.Default.Done, "Finish")
                }
            }
            RecentShots(
                state.value.recentShotsList, onEvent
            )
        }
    }
    if(state.value.is_choix_club_open) {
        ChoixBatonScreen(state, viewModel::onEvent,
            viewModel.newShotAvailable, viewModel.error_gen)
    }
    if(state.value.is_putt_open) {
        PuttScreen(viewModel::onEvent)
    }
    CheckScreen(state = state, onEvent)
}



@Composable
fun RecentShots(
    shotsList: List<ShotRow>, onEvent: (ShotEvent) -> Unit, modifier: Modifier = Modifier
) {
    LazyColumn(modifier = modifier) {
        items(items = shotsList, key = {it.id}) {item ->
            ShotsItem(shot_row = item, onEvent = onEvent)
        }
    }
}


@Composable
private fun ShotsItem(
    shot_row: ShotRow, onEvent: (ShotEvent) -> Unit, modifier: Modifier = Modifier
) {
    Column {
        Row {
            Text(text = shot_row.shot)
            Spacer(Modifier.weight(1f))
            Text(text = "success : " + shot_row.success.toString())
            Spacer(Modifier.weight(1f))
            Text(text = "green : " + shot_row.green.toString())
        }
    }
    Row {
        Text(text = "penalty : " + shot_row.penalty.toString())
        Spacer(Modifier.weight(1f))
        Text(text = "reset : " + shot_row.reset.toString())
        Spacer(Modifier.weight(1f))
        Text(text = "is_putt : " + shot_row.is_putt.toString())
        Spacer(Modifier.weight(1f))
        Button(onClick = {
            onEvent(ShotEvent.DeleteRecordedShot(shot_row))
        }) {
            Icon(Icons.Default.Delete, contentDescription = "Delete")
        }
    }
}


@Composable
fun ChoixBatonScreen(state: State<ShotState>, onEvent: (ShotEvent) -> Unit,
                     newShotAvailable: ShotsAvailableRow, error_gen: Boolean) {
    if(!state.value.is_edit_choix_club_open) {
        Column {
            Text("Choix baton")
            if(state.value.is_delete_option) {
                LazyVerticalGrid(columns = GridCells.Fixed(3)) {
                    items(items = state.value.shotavailableList, key = {it.shot}) {item ->
                        Button(onClick = {
                            onEvent(ShotEvent.onEditExistingShotAvailable(item))
                        }) {
                            Text(item.shot)
                            Icon(Icons.Default.Edit,  "Edit")
                        }
                    }
                }
            } else {
                LazyVerticalGrid(columns = GridCells.Fixed(3)) {
                    items(items = state.value.shotavailableList, key = {it.shot}) { item ->
                        Button(onClick = {
                            onEvent(ShotEvent.OnChooseShot(item.shot))
                        }) {
                            Text(item.shot)
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
                        Text("Set Default")
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
    if(state.value.is_success_open) {
        CheckBody(nom = "Successful shot?",
            onEvent, ShotEvent::OnChangedsucess)
    }
    if(state.value.is_green_open) {
        CheckBody(nom = "Is on the green?",
            onEvent, ShotEvent::OnChangedgreen)
    }
    if(state.value.is_penalty_open) {
        CheckBody(nom = "Got a penalty?",
            onEvent, ShotEvent::OnChangedpenalty)
    }
    if(state.value.is_reset_open) {
        CheckBody(nom = "Had to replay the shot (< min(75% of yards, 100y))?",
            onEvent, ShotEvent::OnChangedreset)
    }
}

@Composable
fun CheckBody(nom: String, onEvent: (ShotEvent) -> Unit, event: (Int) -> ShotEvent) {
    Column {
        Text("${nom}")
        Button(onClick = {
            onEvent(event(2))
        }) {
            Icon(Icons.Default.Done, "Yes")
        }
        Button(onClick = {
            onEvent(event(1))
        }) {
            Text("?")
            //TODO ajouter l'icône téléchargé du point d'interrogation. question_mark-24
            //val imageView: ImageView = findViewById(R.id.custom_icon)
            //imageView.setImageResource(R.drawable.ic_custom_icon)
        }
        Button(onClick = {
            onEvent(event(0))
        }) {
            Icon(Icons.Default.Close, "No")
        }
    }
}