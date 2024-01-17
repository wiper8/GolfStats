package com.example.golfstats.ui

fun check_int(n: Int) : String {

    if(n == 0) return ""
    return n.toString()
}

fun check_string_to_int(s: String) : Int {
    if(s == "") return 0
    if(s == "-") return 0
    return s.toInt()
}
