package com.example.golfstats.ui.Sessions

import android.util.Log
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.golfstats.data.Course.CourseRepo
import com.example.golfstats.data.Course.CourseRow
import com.example.golfstats.data.Holes.HoleRow
import com.example.golfstats.data.Holes.HolesRepo
import com.example.golfstats.data.Recommendations.RecommendationsRepo
import com.example.golfstats.data.Sessions.SessionRow
import com.example.golfstats.data.Sessions.SessionsRepo
import com.example.golfstats.data.Shots.ShotsRepo
import kotlinx.coroutines.cancel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class SessionsViewModel(val sessionsRepo: SessionsRepo, val shotsRepo: ShotsRepo,
                        val courseRepo: CourseRepo, val holesRepo: HolesRepo) : ViewModel() {

    private val _state = MutableStateFlow(SessionsState())

    val state = combine(
        _state,
        sessionsRepo.getSessions(),
        shotsRepo.getShots(),
        courseRepo.getCourses(),
        holesRepo.getHoles()
    ) { state, sessionsList, allShots, allCourses, allHoles ->
        state.copy(
            sessionsList = sessionsList,
            allShots = allShots,
            allCourses = allCourses,
            allHoles = allHoles
        )
    }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000L), SessionsState())

    var newRow: SessionRow by mutableStateOf(SessionRow())
        private set
    var newCourseRow: CourseRow by mutableStateOf(CourseRow())
        private set
    var current_selected_course: CourseRow by mutableStateOf(CourseRow())
        private set
    var newCourseHoles = mutableStateListOf<HoleRow>()
        private set

    private fun validateInput(row: SessionRow): Boolean {
        return row.date.isNotBlank() && row.type.isNotBlank()
    }

    private fun validateCourseInput(row: CourseRow): Boolean {
        return row.nom.isNotBlank() && row.holes > 0 && row.nom != "range"
    }

    private fun validateCourseHoles(l: List<HoleRow>): Boolean {
        l.forEachIndexed { i, e ->
            if (e.par == 0) {
                return false
            }
        }
        return true
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
                newCourseHoles.clear()
                viewModelScope.launch {
                    _state.update {
                        it.copy(
                            is_new_screen_open = false
                        )
                    }
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
            SessionEvent.SetSessionTypeCourse -> {
                newRow = newRow.copy(
                    type = "course"
                )
            }
            SessionEvent.SaveSession -> {
                newRow?.let { row ->
                    if (validateInput(row)) {
                        viewModelScope.launch {
                            sessionsRepo.upsert(row)
                            sessionsRepo.getSessionIdFromDate(newRow.date).collect {l ->
                                _state.update {
                                    it.copy(
                                        session_id = l.id
                                    )
                                }
                                holesRepo.getCourseHoles(state.value.course_id).collect { l ->
                                    _state.update {
                                        it.copy(
                                            is_card_screen_open = true,
                                            is_new_screen_open = false,
                                            is_new_course_screen_open = false,
                                            holesCurr_Course = l
                                        )
                                    }
                                }

                                cancel()
                            }
                        }
                    }
                }
            }
            SessionEvent.SaveSessionRange -> {
                newRow?.let { row ->
                    if (validateInput(row)) {
                        viewModelScope.launch {
                            sessionsRepo.upsert(row)
                            sessionsRepo.getSessionIdFromDate(newRow.date).collect {l ->
                                _state.update {
                                    it.copy(
                                        session_id = l.id,
                                        is_session_id_found = true,
                                        is_new_screen_open = false
                                    )
                                }
                                cancel()
                            }
                        }
                    }
                }
            }
            SessionEvent.ExitRangeScreen -> {
                _state.update {
                    it.copy(
                        is_session_id_found = false,
                        is_new_screen_open = false
                    )
                }
            }
            is SessionEvent.ResumeSession -> {
                viewModelScope.launch {
                    sessionsRepo.getSessionIdFromDate(event.sessionRow.date).collect {l ->
                        _state.update {
                            it.copy(
                                session_id = l.id,
                                course_id = l.course_id
                            )
                        }
                        holesRepo.getCourseHoles(state.value.course_id).collect { l ->
                            _state.update {
                                it.copy(
                                    is_card_screen_open = true,
                                    is_new_screen_open = false,
                                    is_new_course_screen_open = false,
                                    holesCurr_Course = l
                                )
                            }
                        }
                        cancel()
                    }
                }
            }
            is SessionEvent.ResumeSessionRange -> {
                viewModelScope.launch {
                    sessionsRepo.getSessionIdFromDate(event.sessionRow.date).collect {l ->
                        _state.update {
                            it.copy(
                                session_id = l.id,
                                is_session_id_found = true,
                                is_new_screen_open = false
                            )
                        }
                        cancel()
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
            SessionEvent.OnAddNewCourseClick -> {
                _state.update {
                    it.copy(
                        is_new_course_screen_open = true,
                        is_new_screen_open = false
                    )
                }
                newCourseRow = CourseRow()
            }

            is SessionEvent.DeleteCourse -> {
                viewModelScope.launch {

                    holesRepo.deleteHolesFromCourse(event.row.id)
                    courseRepo.delete(event.row)
                    newCourseRow = CourseRow()
                    newCourseHoles.clear()
                }
            }
            SessionEvent.DismissCourse -> {
                _state.update {
                    it.copy(
                        is_new_screen_open = true,
                        is_new_course_screen_open = false
                    )
                }
            }

            SessionEvent.SaveCourse -> {

                viewModelScope.launch {
                    newCourseRow?.let { row ->
                        if (validateCourseInput(row) && validateCourseHoles(newCourseHoles)) {

                            courseRepo.upsert(row)

                            courseRepo.getCoursefromNom(row.nom).collect { l ->

                                val id = l!!.id
                                newCourseHoles.forEachIndexed { i, e ->
                                    holesRepo.upsert(
                                        HoleRow(
                                            course_id = id,
                                            numero = e.numero,
                                            yards = e.yards,
                                            par = e.par,
                                            mypar = e.mypar,
                                            myhandicap = e.myhandicap,
                                            plan = e.plan,
                                            plan_safe = e.plan_safe,
                                            plan_me = e.plan_me
                                        )
                                    )
                                }

                                courseRepo.getCoursefromNom(newCourseRow.nom).collect { l ->

                                    val id = l!!.id
                                    holesRepo.getCourseHoles(id).collect { l ->
                                        l.forEachIndexed {i, e ->
                                            newCourseHoles[i] = e
                                        }
                                        _state.update {
                                            it.copy(
                                                is_add_recommendations_screen_open = true
                                            )
                                        }
                                        cancel()
                                    }

                                }
                            }
                        }
                    }
                }
            }

            is SessionEvent.EditCourse -> {

                newCourseRow = event.row
                newCourseHoles.clear()

                viewModelScope.launch {
                    holesRepo.getCourseHoles(event.row.id).collect { l ->
                        for (i in l.indices) {
                            newCourseHoles.add(l[i])
                        }

                        _state.update {
                            it.copy(
                                is_new_course_screen_open = true,
                                is_new_screen_open = false
                            )
                        }
                        cancel()

                    }
                }
            }
            is SessionEvent.OnChangednomCourse -> {
                newCourseRow = newCourseRow.copy(
                    nom = event.nom,
                )
            }
            SessionEvent.SaveCourse -> {

                viewModelScope.launch {
                    newCourseRow?.let { row ->
                        if (validateCourseInput(row) && validateCourseHoles(newCourseHoles)) {
                            /*_state.update {
                                it.copy(
                                    is_new_screen_open = true,
                                    is_new_course_screen_open = false
                                )
                            }*/
                            courseRepo.upsert(row)

                            courseRepo.getCoursefromNom(row.nom).collect { l ->

                                val id = l!!.id
                                newCourseHoles.forEachIndexed { i, e ->
                                    holesRepo.upsert(
                                        HoleRow(
                                            course_id = id,
                                            numero = e.numero,
                                            yards = e.yards,
                                            par = e.par,
                                            mypar = e.mypar,
                                            myhandicap = e.myhandicap,
                                            plan = e.plan,
                                            plan_safe = e.plan_safe,
                                            plan_me = e.plan_me
                                        )
                                    )
                                }

                                courseRepo.getCoursefromNom(newCourseRow.nom).collect { l ->

                                    val id = l!!.id
                                    holesRepo.getCourseHoles(id).collect { l ->
                                        l.forEachIndexed {i, e ->
                                            newCourseHoles[i] = e
                                        }
                                        _state.update {
                                            it.copy(
                                                is_add_recommendations_screen_open = true
                                            )
                                        }
                                        cancel()
                                    }

                                }
                            }
                        }
                    }
                }
            }
            SessionEvent.SaveRecomm -> {
                _state.update {
                    it.copy(
                        is_add_recommendations_screen_open = false,
                        is_new_screen_open = true,
                        is_new_course_screen_open = false
                    )
                }
                newCourseRow = CourseRow()
                newCourseHoles.clear()
            }

            is SessionEvent.PlayCourse -> {
                newRow = newRow.copy(
                    course_id = event.course_id
                )
                _state.update {
                    it.copy(
                        course_id = event.course_id
                    )
                }
            }

            is SessionEvent.OnChangeNumHoles -> {
                newCourseRow = newCourseRow.copy(
                    holes = event.num
                )
                newCourseHoles.clear()
                for (i in 1..newCourseRow.holes) {
                    val myHole = HoleRow(course_id = newCourseRow.id, numero = i, yards = 0, par = 0)
                    newCourseHoles.add(myHole)
                }
            }

            is SessionEvent.OnChangePar -> {
                newCourseHoles[event.hole - 1] = newCourseHoles[event.hole - 1].copy(
                    par = event.num
                )
            }
            is SessionEvent.OnChangeYards -> {
                newCourseHoles[event.hole - 1] = newCourseHoles[event.hole - 1].copy(
                    yards = event.num
                )
            }

            SessionEvent.Settings -> {
                _state.update {
                    it.copy(
                        is_settings_open = true
                    )
                }
            }
            is SessionEvent.SetSettings -> {
                _state.update {
                    it.copy(
                        is_score_total_visible = event.bool,
                        is_settings_open = false
                    )
                }
            }

            SessionEvent.ExitCard -> {
                _state.update {
                    it.copy(
                        are_shots_calculated = false
                    )
                }
            }

            is SessionEvent.CalculateScores -> {
                viewModelScope.launch {
                    sessionsRepo.getItem(event.session_id).collect {session ->
                        if (session != null) {
                            courseRepo.getItem(session.course_id).collect {course ->
                                if (course != null) {
                                    current_selected_course = course
                                    var nombres_coups = MutableList<Int>(current_selected_course.holes) { 0 }
                                    var total_shots_ = 0
                                    var total_shots_under_par_ = 0
                                    shotsRepo.getSessionShots(event.session_id).collect { l ->
                                        for (hole_i in nombres_coups.indices) {
                                            for (i in l.indices) {
                                                if(l[i].is_putt && l[i].num_hole-1 == hole_i) {
                                                    nombres_coups[hole_i] = nombres_coups[hole_i] + l[i].success
                                                } else {
                                                    if(l[i].num_hole-1 == hole_i) nombres_coups[hole_i]++
                                                    if(l[i].num_hole-1 == hole_i && l[i].penalty == 2) nombres_coups[hole_i]++
                                                }
                                            }
                                            total_shots_ = total_shots_ + nombres_coups[hole_i]
                                            if(nombres_coups[hole_i] > 0) {
                                                // si le trou semble avoir été joué
                                                total_shots_under_par_ = total_shots_under_par_ + state.value.holesCurr_Course[hole_i].par
                                            }
                                        }

                                        total_shots_under_par_ = total_shots_ - total_shots_under_par_
                                        _state.update {
                                            it.copy(
                                                scores_holes = nombres_coups,
                                                total_shots = total_shots_,
                                                total_shots_under_par = total_shots_under_par_,
                                                are_shots_calculated = true
                                            )
                                        }

                                        cancel()
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}


