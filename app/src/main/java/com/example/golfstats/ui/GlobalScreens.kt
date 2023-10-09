package com.example.golfstats.ui

import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.width
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.unit.dp

fun check_int(n: Int) : String {

    if(n == 0) return ""
    return n.toString()
}

fun check_string_to_int(s: String) : Int {
    if(s == "") return 0
    if(s == "-") return 0
    return s.toInt()
}
