package com.example.golfstats.ui.Sessions

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.golfstats.data.Shots.ShotsRepo
import com.example.golfstats.data.ShotsAvailable.ShotsAvailableRepo
import com.example.golfstats.data.ShotsAvailable.ShotsAvailableRow
import com.example.golfstats.ui.Shots.ShotEvent
import com.example.golfstats.ui.Stats.StatEvent
import com.example.golfstats.ui.Stats.StatsState
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.job
import kotlinx.coroutines.launch


class StatsViewModel(val shotsRepo: ShotsRepo, val shotsavailableRepo: ShotsAvailableRepo) : ViewModel() {
    private val _state = MutableStateFlow(StatsState())

    val state = combine(
        _state,
        shotsRepo.getSessionShots(0),
        shotsavailableRepo.getShots()
    ) { state, shotList, shotsAvailableList ->
        state.copy(
            shotsList = shotList,
            shotsAvailableList = shotsAvailableList
        )
    }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000L), StatsState())

    fun onEvent(event: StatEvent) {
        when(event) {
            is StatEvent.Creation -> {
                viewModelScope.launch {
                    shotsavailableRepo.getShots().collect { l ->

                        _state.update {
                            it.copy(
                                shotsAvailableList = l
                            )
                        }
                    }
                }
            }
            is StatEvent.SetSessionId -> {
                _state.update {
                    it.copy(
                        session_id = event.session_id
                    )
                }
                viewModelScope.launch {
                    shotsRepo.getSessionShots(_state.value.session_id).collect { l ->
                        var maximum = 4
                        l.forEachIndexed {i, e->
                            if(e.shot.length > maximum) {
                                maximum = e.shot.length
                            }
                        }
                        _state.update {
                            it.copy(
                                shotsList = l,
                                longest_shot_name = maximum
                            )
                        }
                    }
                }
            }

            StatEvent.GetStats -> {
                viewModelScope.launch {
                    shotsRepo.getUniqueShots(_state.value.session_id).collect { l ->
                        //ordonner selon l'ordre des id des shotsavailable
                        var new_order = l.toMutableList()
                        var j = 0
                        var k = 0
                        while(true) {
                            if(j == _state.value.shotsAvailableList.size) break
                            if(l.contains(_state.value.shotsAvailableList[j].shot)) {
                               new_order[k] = _state.value.shotsAvailableList[j].shot
                               k++
                            }
                            j++
                        }
                        _state.update {
                            it.copy(
                                uniqueshotsname = new_order
                            )
                        }

                        var success = MutableList<Int>(_state.value.uniqueshotsname.size) { 0 }
                        var successTry = MutableList<Int>(_state.value.uniqueshotsname.size) { 0 }
                        var green = MutableList<Int>(_state.value.uniqueshotsname.size) { 0 }
                        var greenTry = MutableList<Int>(_state.value.uniqueshotsname.size) { 0 }
                        var penalty = MutableList<Int>(_state.value.uniqueshotsname.size) { 0 }
                        var penaltyTry = MutableList<Int>(_state.value.uniqueshotsname.size) { 0 }
                        var reset = MutableList<Int>(_state.value.uniqueshotsname.size) { 0 }
                        var resetTry = MutableList<Int>(_state.value.uniqueshotsname.size) { 0 }
                        var putt = MutableList<Int>(5) { 0 }

                        if (_state.value.uniqueshotsname.size > 0) {

                            _state.value.uniqueshotsname.forEachIndexed { i, shot_unique ->
                                for (shot in _state.value.shotsList) {
                                    if(shot.shot == shot_unique) {

                                        if (shot.is_putt) {
                                            putt[shot.success - 1]++
                                        } else {
                                            if (shot.success == 2) {
                                                success[i]++
                                                successTry[i]++
                                            } else if (shot.success == 0) {
                                                successTry[i]++
                                            }
                                            if (shot.green == 2) {
                                                green[i]++
                                                greenTry[i]++
                                            } else if (shot.green == 0) {
                                                greenTry[i]++
                                            }
                                            if (shot.penalty == 2) {
                                                penalty[i]++
                                                penaltyTry[i]++
                                            } else if (shot.penalty == 0) {
                                                penaltyTry[i]++
                                            }
                                            if (shot.reset == 2) {
                                                reset[i]++
                                                resetTry[i]++
                                            } else if (shot.reset == 0) {
                                                resetTry[i]++
                                            }
                                        }
                                    }
                                }
                            }
                            _state.update {
                                it.copy(
                                    success = success,
                                    successTry = successTry,
                                    green = green,
                                    greenTry = greenTry,
                                    penalty = penalty,
                                    penaltyTry = penaltyTry,
                                    reset = reset,
                                    resetTry = resetTry,
                                    putts = putt
                                )
                            }
                        }
                    }
                }
            }
        }
    }

}