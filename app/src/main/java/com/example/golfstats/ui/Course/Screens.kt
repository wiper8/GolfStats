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
import androidx.compose.runtime.State
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import com.example.golfstats.Screens
import com.example.golfstats.data.Course.CourseRow
import com.example.golfstats.data.ShotsAvailable.ShotsAvailableRow
import com.example.golfstats.data.Yardages.YardageRow
import com.example.golfstats.ui.AppViewModelProvider
import com.example.golfstats.ui.Sessions.SessionEvent
import com.example.golfstats.ui.Shots.ShotEvent
import com.example.golfstats.ui.Shots.ShotState
import com.example.golfstats.ui.check_int
import kotlinx.coroutines.launch



@Composable
fun AvailableCourses(
    coursesList: List<CourseRow>
) {
    LazyColumn() {
        items(items = coursesList, key = { it.id }) { item ->
            CourseItem(row = item)
        }
    }
}



@Composable
private fun CourseItem(
    row: CourseRow, modifier: Modifier = Modifier
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
            //TODO
        }) {
            Text("Delete")
        }
    }
}


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun NewCourseRowScreen(
    state: State<CourseState>,
    newRow: CourseRow,
    onEvent: (CourseEvent) -> Unit,
    navController: NavHostController
) {
    Text("course_id ${newRow.id}")
    Text("TODO c'est ici qu'on crée un nouveau terrain")
    OutlinedTextField(
        value = newRow.nom,
        onValueChange = {
            onEvent(CourseEvent.OnChangednom(it))
        },
        label = { Text("Nom") },
        modifier = Modifier
            .width(150.dp)
    )
}