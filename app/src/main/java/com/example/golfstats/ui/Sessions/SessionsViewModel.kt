package com.example.golfstats.ui.Sessions

import android.util.Log
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.golfstats.data.Course.CourseRepo
import com.example.golfstats.data.Course.CourseRow
import com.example.golfstats.data.Sessions.SessionRow
import com.example.golfstats.data.Sessions.SessionsRepo
import com.example.golfstats.data.Shots.ShotsRepo
import com.example.golfstats.ui.Shots.ShotEvent
import com.example.golfstats.ui.Stats.StatEvent
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class SessionsViewModel(val sessionsRepo: SessionsRepo, val shotsRepo: ShotsRepo, val courseRepo: CourseRepo) : ViewModel() {

    private val _state = MutableStateFlow(SessionsState())

    val state = combine(
        _state,
        sessionsRepo.getSessions(),
        shotsRepo.getShots(),
        courseRepo.getCourses()
    ) { state, sessionsList, allShots, allCourses ->
        state.copy(
            sessionsList = sessionsList,
            allShots = allShots,
            allCourses = allCourses
        )
    }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000L), SessionsState())

    var newRow: SessionRow by mutableStateOf(SessionRow())
        private set
    var current_selected_course: CourseRow by mutableStateOf(CourseRow())
        private set

    private fun validateInput(row: SessionRow): Boolean {
        return row.date.isNotBlank() && row.type.isNotBlank()
    }

    fun onEvent(event: SessionEvent) {
        when (event) {
            SessionEvent.OnAddNewClick -> {
                _state.update {
                    it.copy(
                        is_new_screen_open = true
                    )
                }
                newRow = SessionRow()
            }
            SessionEvent.Dismiss -> {

                viewModelScope.launch {
                    _state.update {
                        it.copy(
                            is_new_screen_open = false
                        )
                    }
                    delay(300)
                    newRow = SessionRow()
                }
            }
            is SessionEvent.OnChangeddate -> {
                newRow = newRow.copy(
                    date = event.date,
                )
            }
            is SessionEvent.OnChangedtype -> {
                newRow = newRow.copy(
                    type = event.type
                )
            }
            SessionEvent.Save -> {
                newRow?.let { row ->
                    if (validateInput(row)) {
                        viewModelScope.launch {
                            sessionsRepo.upsert(row)
                            delay(300L)
                        }
                        _state.update {
                            it.copy(
                                is_new_screen_open = false
                            )
                        }
                        newRow = SessionRow()
                    }
                }
            }
            is SessionEvent.Edit -> {
                _state.update {
                    it.copy(
                        is_new_screen_open = true
                    )
                }
                newRow = event.row
            }
            is SessionEvent.Delete -> {
                viewModelScope.launch {
                    sessionsRepo.delete(event.row)
                    shotsRepo.getShots().collect {l ->
                        _state.update {
                            it.copy(
                                allShots = l,
                                is_new_screen_open = false
                            )
                        }
                        _state.value.allShots.forEach {
                            if(it.session_id == event.row.id) {

                                shotsRepo.delete(it)
                                //StatEvent.GetStats
                            }
                        }
                    }
                }
            }
        }
    }
}


