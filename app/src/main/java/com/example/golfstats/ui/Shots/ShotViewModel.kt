package com.example.golfstats.ui.Shots

import android.util.Log
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.golfstats.data.Sessions.SessionRow
import com.example.golfstats.data.Shots.ShotRow
import com.example.golfstats.data.ShotsAvailable.ShotsAvailableRepo
import com.example.golfstats.data.Shots.ShotsRepo
import com.example.golfstats.data.ShotsAvailable.ShotsAvailableRow
import com.example.golfstats.ui.Sessions.SessionEvent
import com.example.golfstats.ui.Stats.StatEvent
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking


class ShotViewModel(val shotsRepo: ShotsRepo, val shotavailableRepo: ShotsAvailableRepo) : ViewModel() {

    private val _state = MutableStateFlow(ShotState())

    val state = combine(
        _state,
        shotsRepo.getShots(),
        shotavailableRepo.getShots(),
    ) { state, shotList, shotavailableList ->
        state.copy(
            recentShotsList = shotList,
            shotavailableList = shotavailableList
        )
    }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000L), ShotState())

    var error_gen: Boolean by mutableStateOf(false)
        private set

    var newShot: ShotRow by mutableStateOf(ShotRow(session_id = _state.value.session_id))
        private set

    var newShotAvailable: ShotsAvailableRow by mutableStateOf(ShotsAvailableRow())
        private set

    fun onEvent(event: ShotEvent)  {
        when (event) {
            ShotEvent.SETDEFAULT -> {
                viewModelScope.launch {
                    shotavailableRepo.upsert(
                        ShotsAvailableRow(
                            "Putt", 0, false, false, false, true
                        )
                    )
                    shotavailableRepo.upsert(
                        ShotsAvailableRow(
                            "Chip", 1, false, true, false, false
                        )
                    )
                    shotavailableRepo.upsert(
                        ShotsAvailableRow(
                            "Bunker", 2, true, true, true, false
                        )
                    )
                    shotavailableRepo.upsert(
                        ShotsAvailableRow(
                            "Punch (fer7)", 3, true, true, true, false
                        )
                    )
                    shotavailableRepo.upsert(
                        ShotsAvailableRow(
                            "SW", 4, true, true, true, false
                        )
                    )
                    shotavailableRepo.upsert(
                        ShotsAvailableRow(
                            "GW", 5, true, true, true, false
                        )
                    )
                    shotavailableRepo.upsert(
                        ShotsAvailableRow(
                            "PW", 6, true, true, true, false
                        )
                    )
                    shotavailableRepo.upsert(
                        ShotsAvailableRow(
                            "Fer9", 7, true, true, true, false
                        )
                    )
                    shotavailableRepo.upsert(
                        ShotsAvailableRow(
                            "Fer8", 8, true, true, true, false
                        )
                    )
                    shotavailableRepo.upsert(
                        ShotsAvailableRow(
                            "Fer7", 9, true, true, true, false
                        )
                    )
                    shotavailableRepo.upsert(
                        ShotsAvailableRow(
                            "Fer6", 10, true, true, true, false
                        )
                    )
                    shotavailableRepo.upsert(
                        ShotsAvailableRow(
                            "Fer5", 11, true, true, true, false
                        )
                    )
                    shotavailableRepo.upsert(
                        ShotsAvailableRow(
                            "Fer4", 12, true, true, true, false
                        )
                    )
                    shotavailableRepo.upsert(
                        ShotsAvailableRow(
                            "Driver", 13, true, true, true, false
                        )
                    )
                }
            }
            ShotEvent.onAddShot -> {
                _state.update {
                    it.copy(
                        is_choix_club_open = true,
                        is_add_shot_screen_open = false
                    )
                }
                newShot = ShotRow(session_id = _state.value.session_id)
                newShotAvailable = ShotsAvailableRow()
                error_gen = false
            }
            is ShotEvent.DeleteShotAvailable -> {
                viewModelScope.launch {
                    shotavailableRepo.delete(event.shotavailable)
                    delay(300L) // Animation delay
                }
                viewModelScope.launch {
                    _state.update {
                        it.copy(
                            is_add_shot_screen_open = false,
                            is_choix_club_open = true,
                            is_success_open = false,
                            is_green_open = false,
                            is_penalty_open = false,
                            is_edit_choix_club_open = false,
                            is_set_default_open = false,
                            is_confirm_open = false,
                            is_reset_open = false,
                            is_delete_option = false
                        )
                    }
                    delay(300)
                    newShot = ShotRow(session_id = _state.value.session_id)
                    newShotAvailable = ShotsAvailableRow()
                    error_gen = false
                }
            }
            ShotEvent.DismissShot -> {
                viewModelScope.launch {
                    _state.update {
                        it.copy(
                            is_add_shot_screen_open = true,
                            is_choix_club_open = false,
                            is_success_open = false,
                            is_green_open = false,
                            is_penalty_open = false,
                            is_edit_choix_club_open = false,
                            is_set_default_open = false,
                            is_confirm_open = false,
                            is_reset_open = false,
                            is_delete_option = false,
                            is_putt_open = false
                        )
                    }
                    delay(300)
                    newShot = ShotRow(session_id = _state.value.session_id)
                    newShotAvailable = ShotsAvailableRow()
                    error_gen = false
                }
            }
            ShotEvent.DismissShotAvailable -> {
                viewModelScope.launch {
                    _state.update {
                        it.copy(
                            is_add_shot_screen_open = false,
                            is_choix_club_open = true,
                            is_success_open = false,
                            is_green_open = false,
                            is_penalty_open = false,
                            is_edit_choix_club_open = false,
                            is_set_default_open = false,
                            is_confirm_open = false,
                            is_reset_open = false,
                            is_delete_option = false,
                            is_putt_open = false
                        )
                    }
                    delay(300)
                    newShot = ShotRow(session_id = _state.value.session_id)
                    newShotAvailable = ShotsAvailableRow()
                    error_gen = false
                }
            }
            ShotEvent.OnAddNewShotAvailable -> {
                _state.update {
                    it.copy(
                        is_edit_choix_club_open = true
                    )
                }
            }
            is ShotEvent.onEditExistingShotAvailable -> {
                newShotAvailable = event.shot
                _state.update {
                    it.copy(
                        is_edit_choix_club_open = true,
                        is_delete_option = true
                    )
                }
            }
            ShotEvent.SaveShotAvailable -> {
                newShotAvailable?.let {row ->
                    if(validateShotAvailable(row)) {
                        viewModelScope.launch {
                            shotavailableRepo.upsert(row)
                            delay(300L)
                        }
                        _state.update {
                            it.copy(
                                is_choix_club_open = true,
                                is_edit_choix_club_open = false
                            )
                        }
                        newShotAvailable = ShotsAvailableRow()
                    } else {
                        error_gen = true
                    }
                }
            }
            is ShotEvent.ClickButtonGreen -> {
                newShotAvailable = newShotAvailable.copy(
                    green = event.bool
                )
            }
            is ShotEvent.ClickButtonPenalty -> {
                newShotAvailable = newShotAvailable.copy(
                    penalty = event.bool
                )
            }
            is ShotEvent.ClickButtonReset -> {
                newShotAvailable = newShotAvailable.copy(
                    reset = event.bool
                )
            }
            is ShotEvent.onChangeShotAvailableName -> {
                newShotAvailable = newShotAvailable.copy(
                    shot = event.shot
                )
            }
            is ShotEvent.onChangeShotAvailableID -> {
                newShotAvailable = newShotAvailable.copy(
                    id = event.id
                )
            }
            ShotEvent.NewAvailableShotIsPutt -> {
                newShotAvailable = ShotsAvailableRow("Putt",
                    green=false, penalty = false, reset = false, is_putt = true)

            }
            ShotEvent.OnEditShotAvailable -> {
                _state.update {
                    it.copy(
                        is_delete_option = true
                    )
                }
            }
            is ShotEvent.OnChooseShot -> {
                newShot = newShot.copy(
                    shot = event.shot
                )

                viewModelScope.launch{
                    newShotAvailable = shotavailableRepo.getItem(newShot.shot).first()
                }

                if(newShot.shot == "Putt") {
                    _state.update {
                        it.copy(
                            is_putt_open = true,
                            is_choix_club_open = false
                        )
                    }
                } else {
                    _state.update {
                        it.copy(
                            is_success_open = true,
                            is_choix_club_open = false
                        )
                    }
                }
            }
            is ShotEvent.SetSessionId -> {
                viewModelScope.launch {
                    shotsRepo.getSessionShots(event.session_id).collect { l ->
                        _state.update {
                            it.copy(
                                session_id = event.session_id,
                                recentShotsList = l
                            )
                        }
                    }
                }
            }
            is ShotEvent.OnChangedsucess -> {
                newShot = newShot.copy(
                    success = event.type
                )
                if(newShot.success == 2) {
                    if(newShotAvailable.green) {
                        _state.update {
                            it.copy(
                                is_success_open = false,
                                is_green_open = true,
                                is_penalty_open = false,
                                is_reset_open = false
                            )
                        }
                    } else {
                        onEvent(ShotEvent.SaveCurrentShot)
                    }
                } else if(newShot.success == 1) {
                    onEvent(ShotEvent.SaveCurrentShot)
                } else {
                    if(newShotAvailable.penalty) {
                        _state.update {
                            it.copy(
                                is_success_open = false,
                                is_green_open = false,
                                is_penalty_open = true,
                                is_reset_open = false
                            )
                        }
                    } else if(newShotAvailable.reset) {
                        _state.update {
                            it.copy(
                                is_success_open = false,
                                is_green_open = false,
                                is_penalty_open = false,
                                is_reset_open = true
                            )
                        }
                    } else {
                        onEvent(ShotEvent.SaveCurrentShot)
                    }
                }
            }
            is ShotEvent.OnChangedgreen -> {
                newShot = newShot.copy(
                    green = event.type
                )
                onEvent(ShotEvent.SaveCurrentShot)
            }
            is ShotEvent.OnChangedpenalty -> {
                newShot = newShot.copy(
                    penalty = event.type
                )
                if(newShot.penalty == 2 || newShot.penalty == 1) {
                    onEvent(ShotEvent.SaveCurrentShot)
                } else {
                    if(newShotAvailable.reset) {
                        _state.update {
                            it.copy(
                                is_success_open = false,
                                is_green_open = false,
                                is_penalty_open = false,
                                is_reset_open = true
                            )
                        }
                    } else {
                        onEvent(ShotEvent.SaveCurrentShot)
                    }
                }
            }
            is ShotEvent.OnChangedreset -> {
                newShot = newShot.copy(
                    reset = event.type
                )
                onEvent(ShotEvent.SaveCurrentShot)
            }
            is ShotEvent.OnChangedputt -> {
                newShot = newShot.copy(
                    success = event.type,
                    is_putt = true
                )
                onEvent(ShotEvent.SaveCurrentShot)
            }
            ShotEvent.SaveCurrentShot -> {
                newShot?.let {row ->
                    viewModelScope.launch {
                        shotsRepo.upsert(row)
                        delay(300L)
                    }
                    onEvent(ShotEvent.DismissShot)
                }
            }

            is ShotEvent.DeleteRecordedShot -> {
                viewModelScope.launch {
                    shotsRepo.delete(event.shot)
                }
            }
        }
    }
}
