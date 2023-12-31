@file:OptIn(ExperimentalMaterial3Api::class)

package com.example.golfstats.ui.Sessions

import android.util.Log
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Create
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.State
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import com.example.golfstats.Screens
import com.example.golfstats.data.Sessions.SessionRow
import com.example.golfstats.data.Sessions.SessionsRepo
import com.example.golfstats.ui.AppViewModelProvider
import com.example.golfstats.ui.ButtonEditDel
import kotlin.reflect.KFunction1

@Composable
fun SessionsScreen(
    navController: NavHostController,
    onNavClick: (Int) -> Unit
) {
    val viewModel: SessionsViewModel = viewModel(factory = AppViewModelProvider.Factory)
    val state = viewModel.state.collectAsState()
    val onEvent = viewModel::onEvent

    if(!state.value.is_new_screen_open) {

        Column {
            SessionsList(sessionsList = state.value.sessionsList,
                onEvent = onEvent, onNavClick = onNavClick)
            Row {
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
    } else {
        NewSessionRowScreen(newRow = viewModel.newRow, onEvent = onEvent)
    }
}

@Composable
fun StatSessionsScreen(
    navController: NavHostController,
    onNavClick: (Int) -> Unit
) {
    val viewModel: SessionsViewModel = viewModel(factory = AppViewModelProvider.Factory)
    val state = viewModel.state.collectAsState()
    val onEvent = viewModel::onEvent

    Column {
        StatSessionsList(sessionsList = state.value.sessionsList,
            onEvent = onEvent, onNavClick = onNavClick)
        Row {
            Button(onClick = {
                navController.popBackStack(route = Screens.Menu.name, inclusive = false)
            }) {
                Icon(Icons.Default.ArrowBack, "return")
            }
            Button(onClick = {
                onNavClick(-1)
            }) {
                Text("All stats")
            }
        }
    }
}

@Composable
private fun SessionsList(
    sessionsList: List<SessionRow>, onEvent: (SessionEvent) -> Unit,
    onNavClick: (Int) -> Unit, modifier: Modifier = Modifier
) {
    LazyColumn(modifier = modifier) {
        items(items = sessionsList, key = { it.id }) { item ->
            SessionItem(row = item, onEvent = onEvent, onNavClick = onNavClick)
        }
    }
}

@Composable
private fun SessionItem(
    row: SessionRow, onEvent: (SessionEvent) -> Unit,
    onNavClick: (Int) -> Unit, modifier: Modifier = Modifier
) {
    Row(
        modifier = Modifier.fillMaxWidth()
    ) {
        Text(
            text = row.date,
            style = MaterialTheme.typography.titleLarge,
        )
        Spacer(Modifier.weight(1f))
        Text(text = "ID : ${row.id}")
        Button(onClick = {
            onNavClick(row.id)
        }) {
            Icon(Icons.Default.PlayArrow, contentDescription = "Play")
        }
        Button(onClick = {
            onEvent(SessionEvent.Edit(row))
            //navController.navigate(Screens.NewYardageRow.name)
        }) {
            Icon(Icons.Default.Create, contentDescription = "Edit")
        }
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
        modifier = Modifier.fillMaxWidth()
    ) {
        Text(
            text = row.date,
            style = MaterialTheme.typography.titleLarge,
        )
        Spacer(Modifier.weight(1f))
        Text(text = "ID : ${row.id}")
        Button(onClick = {
            onNavClick(row.id)
        }) {
            Text("Stats")
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun NewSessionRowScreen(
    newRow: SessionRow,
    onEvent: (SessionEvent) -> Unit
) {
    Column {
        Column {
            OutlinedTextField(
                value = newRow.date,
                onValueChange = {
                    onEvent(SessionEvent.OnChangeddate(it))
                },
                label = { Text("Date") },
                modifier = Modifier
                    .width(150.dp)
            )
        }
        ButtonEditDel({
            onEvent(SessionEvent.Dismiss)
        }, {
            onEvent(SessionEvent.Save)
        })
    }
}
