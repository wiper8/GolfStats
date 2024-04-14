package com.example.golfstats.ui.Yardages

import com.example.golfstats.data.Yardages.YardageRow

sealed interface YardageEvent {
    object OnAddNewClick: YardageEvent
    object Dismiss: YardageEvent
    data class OnChangedbaton(val baton: String): YardageEvent
    data class OnChangedninety(val ninety: Int): YardageEvent
    data class OnChangedhundred(val hundred: Int): YardageEvent
    object Save: YardageEvent
    data class Edit(val row: YardageRow): YardageEvent
    data class Delete(val row: YardageRow): YardageEvent
    object SETDEFAULT: YardageEvent
}