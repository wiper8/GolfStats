@file:OptIn(ExperimentalMaterial3Api::class)

package com.example.golfstats.ui

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.width
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Done
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FilledTonalButton
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController

@Composable
fun MenuScreen(navController: NavHostController, modifier: Modifier = Modifier
    .width(260.dp)
    .height(160.dp)) {

    Column(
        Modifier.fillMaxSize(),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Spacer(Modifier.height(13.dp))
        Button(onClick = {
            navController.navigate("yardages_graph")
        },
            modifier = modifier) {
            Text(text = "Yardages", fontSize = 36.sp)
        }
        Spacer(Modifier.height(27.dp))
        Button(onClick = {
            navController.navigate("course_graph")
        },
            modifier = modifier) {
            Text(text="Scorecard+", fontSize = 36.sp)
        }
        Spacer(Modifier.height(27.dp))
        Button(onClick = {
            navController.navigate("range_graph")
        },
            modifier = modifier) {
            Column(
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(text="Range", fontSize = 36.sp)
                Spacer(Modifier.height(10.dp))
                Text(text="Tracker", fontSize = 36.sp)
            }
        }
        Spacer(Modifier.height(27.dp))
        Button(onClick = {
            navController.navigate("stats_graph")
        },
            modifier = modifier) {
            Text(text="Stats", fontSize = 36.sp)
        }
        Spacer(Modifier.height(13.dp))
        Button(onClick = {
            navController.navigate("databases_graph")
        },
            modifier = Modifier.height(80.dp).width(150.dp)) {
            Text(text="Databases", fontSize = 20.sp)
        }
        Spacer(Modifier.height(13.dp))
    }
}

@Composable
fun ButtonEditDel(
    deleteEvent: () -> Unit,
    saveEvent: () -> Unit,
    saveCondition: Boolean
) {
    Row {
        Button(onClick = deleteEvent) {
            Icon(Icons.Default.Close, contentDescription = "Cancel")
        }
        if(saveCondition) {
            Button(
                onClick = saveEvent
            ) {
                Icon(Icons.Default.Done, contentDescription = "Save")
            }
        } else {
            FilledTonalButton(onClick = {}) {
                Icon(Icons.Default.Done, contentDescription = "Save")
            }
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
                        .width(180.dp),
                colors = TextFieldDefaults.outlinedTextFieldColors(
                    textColor = Color.DarkGray
                ))
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

