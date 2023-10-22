package com.example.golfstats.ui.Yardages

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
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Create
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Done
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material.icons.rounded.Add
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import com.example.golfstats.Screens
import com.example.golfstats.data.Yardages.YardageRow
import com.example.golfstats.ui.AppViewModelProvider
import com.example.golfstats.ui.Shots.ShotEvent
import com.example.golfstats.ui.check_int
import kotlinx.coroutines.launch


@Composable
fun YardagesScreen(
    navController: NavHostController
) {
    val viewModel: YardageViewModel = viewModel(factory = AppViewModelProvider.Factory)
    val state = viewModel.state.collectAsState()
    val onEvent = viewModel::onEvent

    if(!state.value.is_new_screen_open) {

        Column() {
            YardageList(yardageList = state.value.yardageList, onEvent = onEvent)

            Row {
                Button(onClick = {
                    navController.popBackStack(route = Screens.Yardages.name, inclusive = true)
                }) {
                    Icon(Icons.Default.ArrowBack, "return")
                }
                Button(onClick = {
                    onEvent(YardageEvent.SETDEFAULT)
                }) {
                    Text("Set Default", fontSize=20.sp)
                    Icon(Icons.Default.Refresh, contentDescription = "Set Default")
                }
                Button(onClick = {
                    onEvent(YardageEvent.OnAddNewClick)
                    //navController.navigate(Screens.NewYardageRow.name)
                }) {
                    Icon(Icons.Default.Add, contentDescription = "Add")
                }
            }
        }

    } else {
        NewYardageRowScreen(newRow = viewModel.newRow, onEvent = onEvent)
    }

}

@Composable
private fun YardageList(
    yardageList: List<YardageRow>, onEvent: (YardageEvent) -> Unit, modifier: Modifier = Modifier
) {
    LazyColumn(modifier = modifier) {
        items(items = yardageList, key = { it.baton }) { item ->
            YardageItem(yardagerow = item, onEvent = onEvent)
        }
    }
}

@Composable
private fun YardageItem(
    yardagerow: YardageRow, onEvent: (YardageEvent) -> Unit, modifier: Modifier = Modifier
) {
    Row(
        modifier = Modifier.fillMaxWidth()
    ) {
        Text(
            text = yardagerow.baton,
            style = MaterialTheme.typography.titleLarge,
        )
        Spacer(Modifier.weight(1f))
        Text(text = yardagerow.ninety.toString())
        Spacer(Modifier.weight(1f))
        Text(text = yardagerow.hundred.toString())
        Button(onClick = {
            onEvent(YardageEvent.Edit(yardagerow))
            //navController.navigate(Screens.NewYardageRow.name)
        }) {
            Icon(Icons.Default.Create, contentDescription = "Edit")
        }
        Button(onClick = {
            onEvent(YardageEvent.Delete(yardagerow))
        }) {
            Icon(Icons.Default.Delete, contentDescription = "Delete")
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun NewYardageRowScreen(
    newRow: YardageRow,
    onEvent: (YardageEvent) -> Unit,
    modifier: Modifier = Modifier
) {

    Column {
        Column {
            OutlinedTextField(
                value = newRow.baton,
                onValueChange = {
                    onEvent(YardageEvent.OnChangedbaton(it))
                    //viewModel.updateUiState(viewModel.UiState.yardageDetails.copy(baton = it))
                },
                label = {Text("Bâton")},
                modifier = Modifier
                    .width(150.dp)
            )
            OutlinedTextField(
                value = check_int(newRow.ninety),
                onValueChange = {
                    onEvent(YardageEvent.OnChangedninety(it.toInt()))
                    //viewModel.updateUiState(viewModel.UiState.yardageDetails.copy(ninety = it.toInt()))
                },
                label = {Text("90%")},
                modifier = Modifier
                    .width(150.dp))
            OutlinedTextField(
                value = check_int(newRow.hundred),
                onValueChange = {
                    onEvent(YardageEvent.OnChangedhundred(it.toInt()))
                    //viewModel.updateUiState(viewModel.UiState.yardageDetails.copy(hundred = it.toInt()))
                },
                label = {Text("100%")},
                modifier = Modifier
                    .width(150.dp))
        }
        Row {
            Button(onClick = {
                onEvent(YardageEvent.Dismiss)
                //navController.popBackStack(route = Screens.NewYardageRow.name, inclusive = true)
                //navController.navigate(Screens.Yardages.name)
            }) {
                Icon(Icons.Default.Close, contentDescription = "Delete")
            }
            Button(
                onClick = {
                    onEvent(YardageEvent.Save)
                    //navController.popBackStack(route = Screens.NewYardageRow.name, inclusive = true)
                    /*coroutineScope.launch {
                        viewModel.saveYardage()
                        navController.popBackStack(route = Screens.NewYardageRow.name, inclusive = true)
                        //navController.navigate(Screens.Yardages.name)
                    }*/
                }
            ) {
                Icon(Icons.Default.Done, contentDescription = "Save")
            }
        }
    }
}