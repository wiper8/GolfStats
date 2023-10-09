package com.example.golfstats.ui.Sessions

import android.util.Log
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.golfstats.data.Sessions.SessionRow
import com.example.golfstats.data.Sessions.SessionsRepo
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class SessionsViewModel(val sessionsRepo: SessionsRepo) : ViewModel() {

    private val _state = MutableStateFlow(SessionsState())

    val state = combine(
        _state,
        sessionsRepo.getSessions()
    ) { state, sessionsList ->
        state.copy(
            sessionsList = sessionsList
        )
    }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000L), SessionsState())

    var newRow: SessionRow by mutableStateOf(SessionRow())
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
                    _state.update {
                        it.copy(
                            is_new_screen_open = false
                        )
                    }
                    sessionsRepo.delete(event.row)
                    delay(300L) // Animation delay
                }
            }
        }
    }
}


