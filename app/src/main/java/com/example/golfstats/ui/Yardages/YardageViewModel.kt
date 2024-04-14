package com.example.golfstats.ui.Yardages

import android.util.Log
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.golfstats.data.Yardages.YardageRow
import com.example.golfstats.data.Yardages.YardagesRepo
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class YardageViewModel(val yardagesRepo: YardagesRepo) : ViewModel() {

    private val _state = MutableStateFlow(YardageState())

    val state = combine(
        _state,
        yardagesRepo.getYardages()
    ) { state, yardageList ->
        state.copy(
            yardageList = yardageList
        )
    }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000L), YardageState())

    var newRow: YardageRow by mutableStateOf(YardageRow())
        private set

    private fun validateInput(row: YardageRow): Boolean {
        return row.baton.isNotBlank() && row.ninety != 0 && row.hundred != 0
    }

    fun onEvent(event: YardageEvent) {
        when(event) {
            YardageEvent.SETDEFAULT -> {
                viewModelScope.launch {
                    yardagesRepo.upsert(
                        YardageRow(
                            "Driver", 190, 220
                        )
                    )
                    yardagesRepo.upsert(
                        YardageRow(
                            "Fer 4", 165, 180
                        )
                    )
                    yardagesRepo.upsert(
                        YardageRow(
                            "Fer 5", 160, 170
                        )
                    )
                    yardagesRepo.upsert(
                        YardageRow(
                            "Fer 6", 150, 160
                        )
                    )
                    yardagesRepo.upsert(
                        YardageRow(
                            "Fer 7", 140, 150
                        )
                    )
                    yardagesRepo.upsert(
                        YardageRow(
                            "Fer 8", 130, 140
                        )
                    )
                    yardagesRepo.upsert(
                        YardageRow(
                            "Fer 9", 115, 125
                        )
                    )
                    yardagesRepo.upsert(
                        YardageRow(
                            "PW", 100, 110
                        )
                    )
                    yardagesRepo.upsert(
                        YardageRow(
                            "GW", 90, 100
                        )
                    )
                    yardagesRepo.upsert(
                        YardageRow(
                            "SW", 70, 85
                        )
                    )

                }
            }
            is YardageEvent.Delete -> {
                viewModelScope.launch {
                    _state.update {
                        it.copy(is_new_screen_open = false)
                    }
                    yardagesRepo.delete(event.row)
                    delay(300L) // Animation delay
                    _state.update { it.copy(
                        current_row = YardageRow()
                    ) }
                }
            }
            YardageEvent.Dismiss -> {
                _state.update { it.copy(
                    is_new_screen_open = false
                ) }
                newRow = YardageRow()
                _state.update { it.copy(
                    current_row = YardageRow()
                ) }
            }
            is YardageEvent.Edit -> {
                _state.update { it.copy(
                    is_new_screen_open = true
                ) }
                newRow = event.row
            }
            YardageEvent.OnAddNewClick -> {
                _state.update { it.copy(
                    is_new_screen_open = true
                ) }
                newRow = YardageRow()
            }
            is YardageEvent.OnChangedbaton -> {
                newRow = newRow.copy(
                    baton = event.baton,
                )
            }
            is YardageEvent.OnChangedninety -> {
                newRow = newRow.copy(
                    ninety = event.ninety,
                )
            }
            is YardageEvent.OnChangedhundred -> {
                newRow = newRow.copy(
                    hundred = event.hundred,
                )
            }
            YardageEvent.Save -> {
                newRow?.let {row ->
                    if(validateInput(row)) {
                        _state.update { it.copy(
                            is_new_screen_open = false
                        ) }
                        viewModelScope.launch {
                            yardagesRepo.upsert(row)
                            delay(300L) // Animation delay
                            newRow = YardageRow()
                        }
                    }
                }
            }
            else -> Unit
        }
    }
}
