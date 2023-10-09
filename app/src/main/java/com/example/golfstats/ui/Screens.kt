@file:OptIn(ExperimentalMaterial3Api::class)

package com.example.golfstats.ui

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.ArrowForward
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Done
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.TextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import com.example.golfstats.Screens
import com.example.golfstats.data.Sessions.SessionRow
import com.example.golfstats.data.Yardages.YardageRow
import com.example.golfstats.ui.Course.CourseEvent
import com.example.golfstats.ui.Sessions.SessionEvent
import com.example.golfstats.ui.Yardages.YardageEvent
import kotlinx.coroutines.launch
import org.w3c.dom.Text

@Composable
fun MenuScreen(navController: NavHostController) {

    Column(
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Button(onClick = {
            navController.navigate("yardages_graph")
        }) {
            Text(text = "Yardages")
        }
        /*Button(onClick = {
            navController.navigate("course_graph")
        }) {
            Text(text="Scorecard+")
        }*/
        Button(onClick = {
            navController.navigate("range_graph")
        }) {
            Text(text="Range Tracker")
        }
        Button(onClick = {
            navController.navigate("stats_graph")
        }) {
            Text(text="Stats")
        }
    }
}

@Composable
fun ButtonEditDel(
    deleteEvent: () -> Unit,
    saveEvent: () -> Unit
) {
    Row {
        Button(onClick = deleteEvent) {
            Icon(Icons.Default.Close, contentDescription = "Cancel")
        }
        Button(
            onClick = saveEvent
        ) {
            Icon(Icons.Default.Done, contentDescription = "Save")
        }
    }
}

//TODO
/*@Composable
fun ConfirmScreen(message: Text) {
    Column {
        Row {
            message
        }
        Row {
            Button(onClick = {}) {
                Text("Cancel")
            }
            Button(onClick = {}) {
                Text("Confirm")
            }
        }
    }
}*/

/*
@Composable
fun NewCourseScreen(navController: NavHostController) {
    Column() {
        var nom_terrain by remember {
            mutableStateOf("")
        }
        var n_trous by remember {
            mutableStateOf(9)
        }
        var front_nine by remember {
            mutableStateOf(true)
        }
        var arrow by remember {
            mutableStateOf(Icons.Default.ArrowForward)
        }

        Row {
            Text(text = "name")
            OutlinedTextField(
                value = nom_terrain,
                onValueChange = { text ->
                    nom_terrain = text
                },
                modifier = Modifier
                        .width(150.dp))
            Button(onClick = {
                n_trous = 9
            }) {
                Text("9")
            }
            Button(onClick = {
                n_trous = 18
            }) {
                Text("18")
            }
        }

        LazyColumn() {
            items(9) { i ->

                Row {
                    Text(text="${
                        if(!front_nine) {
                            i + 9 + 1
                        } else {
                            i + 1
                        }
                    } $n_trous")
                    var par by remember {
                        mutableStateOf(4)
                    }

                    var yards by remember {
                        mutableStateOf(0)
                    }
                    var mypar by remember {
                        mutableStateOf(7)
                    }
                    var myhandicap by remember {
                        mutableStateOf(9)
                    }
                    var my_strat by remember {
                        mutableStateOf("7i, 9i, SW")
                    }
                    Button(
                        onClick = {
                            par = if(par == 5) 3 else par+1
                        }
                    ) {
                        Text(text="$par")
                    }
                    TextField(
                        value = check_int(yards),
                        onValueChange = {text ->
                            yards = text.toInt()
                        },
                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Phone),
                        modifier = Modifier
                            .width(60.dp)
                    )

                    Button(
                        onClick = {
                            mypar = if(mypar == 10) 3 else mypar+1
                        }
                    ) {
                        Text(text="$mypar")
                    }
                    Button(
                        onClick = {
                            myhandicap = if(myhandicap == 18) 1 else myhandicap+1
                        }
                    ) {
                        Text(text="$myhandicap")
                    }
                    TextField(
                        value = my_strat,
                        onValueChange = {text ->
                            my_strat = text
                        },
                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Phone),
                        modifier = Modifier
                            .weight(1f)
                    )
                }
            }
        }


        //hole#, par, yards, mypar, myhandicap, mystrat
        Row {
            if(n_trous == 18) {

                ClickableIcon(
                    icon = arrow,
                    onClick = {
                        front_nine = !front_nine
                        if(front_nine) {
                            arrow = Icons.Default.ArrowBack
                        } else {
                            arrow = Icons.Default.ArrowForward
                        }

                    }
                )
            }
        }
        Button(onClick = {
            navController.navigate(Screens.Course.name)
        }) {
            Text(text = "Save")
        }
    }
}

 */

