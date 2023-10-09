package com.example.golfstats.ui.Shots

import com.example.golfstats.data.Shots.ShotRow
import com.example.golfstats.data.ShotsAvailable.ShotsAvailableRow

//import com.example.golfstats.data.ShotsAvailable.ShotAvailableRow

fun validateShotState(state: ShotState): Boolean {
    var n = 0
    if(state.is_add_shot_screen_open) n += 1
    if(state.is_choix_club_open) n += 1
    if(state.is_edit_choix_club_open) n += 1
    if(state.is_success_open) n += 1
    if(state.is_green_open) n += 1
    if(state.is_penalty_open) n += 1
    if(state.is_reset_open) n += 1
    if(state.is_set_default_open) n += 1
    return n == 1
}

fun validateShotAvailable(shot: ShotsAvailableRow): Boolean {
    if(shot.shot.isBlank()) return false
    if(shot.is_putt) {
        if(shot.shot == "Putt" && !shot.green && !shot.penalty && !shot.reset) {
            return true
        }
        return false
    }
    if(shot.shot == "Chip") {
        if(!shot.green && shot.penalty && !shot.reset) return true
        return false
    }
    return true
}


data class ShotState(
    val session_id: Int = 0,
    val recentShotsList: List<ShotRow> = listOf(),
    val allShotsList: List<ShotRow> = listOf(),
    val shotavailableList: List<ShotsAvailableRow> = listOf(),
    var current_row: ShotRow = ShotRow(session_id = session_id),
    var is_add_shot_screen_open: Boolean = true,
    var is_choix_club_open: Boolean = false,
    val is_edit_choix_club_open: Boolean = false,
    val is_delete_option: Boolean = false,
    var is_putt_open: Boolean = false,
    var is_success_open: Boolean = false,
    var is_green_open: Boolean = false,
    var is_penalty_open: Boolean = false,
    var is_reset_open: Boolean = false,
    var is_set_default_open: Boolean = false,
    val is_confirm_open: Boolean = false
)