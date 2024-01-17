package com.example.golfstats.ui.Databases

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.golfstats.data.Course.CourseRepo
import com.example.golfstats.data.Holes.HolesRepo
import com.example.golfstats.data.Sessions.SessionsRepo
import com.example.golfstats.data.Shots.ShotsRepo
import com.example.golfstats.data.ShotsAvailable.ShotsAvailableRepo
import com.example.golfstats.data.Yardages.YardagesRepo
import com.example.golfstats.ui.Shots.ShotState
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update

class DatabasesViewModel(
    val coursesRepo: CourseRepo,
    val holesRepo: HolesRepo,
    val shotsRepo: ShotsRepo,
    val sessionsRepo: SessionsRepo) : ViewModel() {

    private val _state = MutableStateFlow(DatabasesState())

    val state = combine(
        _state,
        coursesRepo.getCourses(),
        holesRepo.getHoles(),
        shotsRepo.getShots(),
        sessionsRepo.getSessions()
    ) { state, courses, holes, shots, sessions ->
        state.copy(
            courses = courses,
            holes = holes,
            shots = shots,
            sessions = sessions
        )
    }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000L), DatabasesState())


    fun onEvent(event: DatabasesEvent) {
        when (event) {
            DatabasesEvent.Dismiss -> {_state.update {it.copy(which_db_open = 0)}}
            DatabasesEvent.ClickCourses -> {_state.update {it.copy(which_db_open = 1)}}
            DatabasesEvent.ClickHoles -> {_state.update {it.copy(which_db_open = 2)}}
            DatabasesEvent.ClickShots -> {_state.update {it.copy(which_db_open = 3)}}
            DatabasesEvent.ClickSessions -> {_state.update {it.copy(which_db_open = 4)}}
        }
    }
}
