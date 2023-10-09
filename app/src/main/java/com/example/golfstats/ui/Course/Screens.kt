package com.example.golfstats.ui.Course

import CourseViewModel
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import com.example.golfstats.Screens
import com.example.golfstats.data.Course.CourseRow
import com.example.golfstats.data.Yardages.YardageRow
import com.example.golfstats.ui.AppViewModelProvider
import com.example.golfstats.ui.check_int
import kotlinx.coroutines.launch


/*
@Composable
fun CourseScreen(
    navController: NavHostController,
    viewModel: CourseViewModel = viewModel(factory = AppViewModelProvider.Factory)
) {
    val UiState by viewModel.UiState.collectAsState()

    Column() {
        CourseList(courseList = UiState.courseList)
        Row {
            Button(onClick = {
                navController.navigate(Screens.NewCourse.name)
            }) {
                Text(text = "Add")
            }
        }
    }
}

@Composable
private fun CourseList(
    courseList: List<CourseRow>, modifier: Modifier = Modifier
) {
    LazyColumn(modifier = modifier) {
        items(items = courseList, key = { it.id }) { item ->
            CourseItem(row = item)
        }
    }
}

@Composable
private fun CourseItem(
    row: CourseRow, modifier: Modifier = Modifier,
    viewModel: CourseEntryViewModel = viewModel(factory = AppViewModelProvider.Factory)
) {
    val coroutineScope = rememberCoroutineScope()

    Row(
        modifier = Modifier.fillMaxWidth()
    ) {
        Text(
            text = row.nom,
            style = MaterialTheme.typography.titleLarge,
        )
        Spacer(Modifier.weight(1f))
        Text(text = row.holes.toString())
        Button(onClick = {

        }) {
            Text("Edit")
        }
        Button(onClick = {
            coroutineScope.launch {
                viewModel.delete(row)
            }
        }) {
            Text("Delete")
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun NewCourseRowScreen(
    navController: NavHostController, modifier: Modifier = Modifier,
    viewModel: CourseEntryViewModel = viewModel(factory = AppViewModelProvider.Factory)
) {
    val coroutineScope = rememberCoroutineScope()
    Column {
        Column {
            OutlinedTextField(
                value = viewModel.UiState.courseDetails.nom,
                onValueChange = {
                    viewModel.updateUiState(viewModel.UiState.courseDetails.copy(nom = it))
                },
                label = {Text("Nom")},
                modifier = Modifier
                    .width(150.dp)
            )
            Row {
                Button(onClick = {

                }){
                    Text("9")
                }
                Button(onClick = {

                }){
                    Text("18")
                }
            }
        }
        Row {
            Button(onClick = {
                navController.popBackStack(route = Screens.NewCourse.name, inclusive = true)
                //navController.navigate(Screens.Yardages.name)
            }) {
                Text("Cancel")
            }
            Button(
                onClick = {
                    coroutineScope.launch {
                        viewModel.save()
                    }
                    navController.popBackStack(route = Screens.NewCourse.name, inclusive = true)
                    //navController.navigate(Screens.Yardages.name)
                }
            ) {
                Text("Save")
            }
        }
    }
}
*/