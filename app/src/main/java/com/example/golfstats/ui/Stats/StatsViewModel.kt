package com.example.golfstats.ui.Sessions

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.golfstats.data.Shots.ShotsRepo
import com.example.golfstats.ui.Shots.ShotEvent
import com.example.golfstats.ui.Stats.StatEvent
import com.example.golfstats.ui.Stats.StatsState
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch


class StatsViewModel(val shotsRepo: ShotsRepo) : ViewModel() {
    private val _state = MutableStateFlow(StatsState())

    val state = combine(
        _state,
        shotsRepo.getSessionShots(0),
    ) { state, shotList ->
        state.copy(
            shotsList = shotList
        )
    }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000L), StatsState())

    fun onEvent(event: StatEvent) {
        when(event) {
            is StatEvent.SetSessionId -> {
                _state.update {
                    it.copy(
                        session_id = event.session_id
                    )
                }
                viewModelScope.launch {
                    shotsRepo.getSessionShots(_state.value.session_id).collect{l ->
                        _state.update {
                            it.copy(
                                shotsList = l
                            )
                        }
                    }
                }

            }

            is StatEvent.GetUniqueShotName -> {
                viewModelScope.launch {
                    shotsRepo.getUniqueShots(_state.value.session_id).collect{l ->
                        _state.update {
                            it.copy(
                                uniqueshotsname = l
                            )
                        }
                    }
                }
            }

            is StatEvent.GetStatSuccess -> {
                viewModelScope.launch {
                    var tmp_list = mutableListOf<Int>()

                    Log.d("EEEEE", "unique shots : ${_state.value.uniqueshotsname}")
                    for (baton in _state.value.uniqueshotsname) {
                        Log.d("EEEEE", "GETSTATSUCCESS INSIDE WITH ${baton}")
                        Log.d("EEEEE", "GETSTATSUCCESS INSIDE WITH LIST : ${tmp_list}")
                        var flow_test =  shotsRepo.getSuccess(event.session_id, baton)
                        Log.d("EEEEE", flow_test.toString())
                        //shotsRepo.getSuccess(event.session_id, baton).collect { i ->
                        //    tmp_list.add(i)
                        //}
                    }
                    Log.d("EEEEE", "après la loop ")
                    _state.update {
                        it.copy(
                            success = tmp_list.toList()
                        )
                    }
                }
            }
            /*
            is StatEvent.GetStatSuccessTry -> {
                for (baton in _state.value.uniqueshotsname) {
                    viewModelScope.launch {
                        shotsRepo.getSuccessTry(event.session_id, baton).collect{l ->
                            _state.update {
                                it.copy(
                                    successTry = it.successTry.plus(l)
                                )
                            }
                        }
                    }
                }
            }

            is StatEvent.GetStatGreen -> {
                viewModelScope.launch {
                    shotsRepo.getGreen(event.session_id).collect{l ->
                        _state.update {
                            it.copy(
                                green = l
                            )
                        }
                    }
                }
            }
            is StatEvent.GetStatGreenTry -> {
                viewModelScope.launch {
                    shotsRepo.getGreenTry(event.session_id).collect{l ->
                        _state.update {
                            it.copy(
                                greenTry = l
                            )
                        }
                    }
                }
            }
            is StatEvent.GetStatPenalty -> {
                viewModelScope.launch {
                    shotsRepo.getPenalty(event.session_id).collect { l ->
                        _state.update {
                            it.copy(
                                penalty = l
                            )
                        }
                    }
                }
            }
            is StatEvent.GetStatPenaltyTry -> {
                viewModelScope.launch {
                    shotsRepo.getPenaltyTry(event.session_id).collect { l ->
                        _state.update {
                            it.copy(
                                penaltyTry = l
                            )
                        }
                    }
                }
            }
            is StatEvent.GetStatReset -> {
                viewModelScope.launch {
                    shotsRepo.getReset(event.session_id).collect { l ->
                        _state.update {
                            it.copy(
                                reset = l
                            )
                        }
                    }
                }
            }
            is StatEvent.GetStatResetTry -> {
                viewModelScope.launch {
                    shotsRepo.getResetTry(event.session_id).collect { l ->
                        _state.update {
                            it.copy(
                                resetTry = l
                            )
                        }
                    }
                }
            }

            is StatEvent.GetStatPutt -> {

            }
            */
            else -> {

            }
        }
    }

}