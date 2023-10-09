
import android.util.Log
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.golfstats.data.Course.CourseRepo
import com.example.golfstats.data.Course.CourseRow
import com.example.golfstats.ui.Course.CourseEvent
import com.example.golfstats.ui.Course.CourseState
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class CourseViewModel(val courseRepo: CourseRepo) : ViewModel() {

    private val _state = MutableStateFlow(CourseState())

    val state = combine(
        _state,
        courseRepo.getCourses()
    ) { state, coursesList ->
        state.copy(
            coursesList = coursesList
        )
    }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000L), CourseState())

    var newRow: CourseRow by mutableStateOf(CourseRow())
        private set

    private fun validateInput(row: CourseRow): Boolean {
        return row.nom.isNotBlank() && row.holes > 0
    }

    fun onEvent(event: CourseEvent) {
        when (event) {
            is CourseEvent.Delete -> {
                viewModelScope.launch {
                    _state.update {
                        it.copy(is_new_screen_open = false)
                    }
                    courseRepo.delete(event.row)
                    delay(300L) // Animation delay
                    _state.update {
                        it.copy(
                            current_row = CourseRow()
                        )
                    }
                }
            }

            CourseEvent.Dismiss -> {
                _state.update {
                    it.copy(
                        is_new_screen_open = false
                    )
                }
                //viewModelScope.launch {
                //_state.value.current_row = YardageRow()
                //}
                //delay(300L)
                newRow = CourseRow()
                _state.update {
                    it.copy(
                        current_row = CourseRow()
                    )
                }
            }

            is CourseEvent.Edit -> {
                _state.update {
                    it.copy(
                        is_new_screen_open = true
                    )
                }
                //viewModelScope.launch {
                //    _state.value.current_row = event.row
                //}
                newRow = event.row
            }

            CourseEvent.OnAddNewClick -> {
                _state.update {
                    it.copy(
                        is_new_screen_open = true
                    )
                }
                //viewModelScope.launch {
                //    _state.value.current_row = YardageRow()
                //}
                newRow = CourseRow()
            }

            is CourseEvent.OnChangednom -> {
                //viewModelScope.launch {
                //    _state.value.current_row.baton = event.baton
                //    _state.value.current_row.ninety = event.ninety
                //    _state.value.current_row.hundred = event.hundred
                //}
                newRow = newRow.copy(
                    nom = event.nom,
                )
            }
            CourseEvent.Save -> {
                /*
                if(validateInput(_state.value.current_row)) {
                    viewModelScope.launch {
                        yardagesRepo.upsert(_state.value.current_row)
                        delay(300L) // Animation delay
                        _state.value.current_row = YardageRow()
                    }
                }*/
                newRow?.let { row ->
                    if (validateInput(row)) {
                        _state.update {
                            it.copy(
                                is_new_screen_open = false
                            )
                        }
                        viewModelScope.launch {
                            courseRepo.upsert(row)
                            delay(300L) // Animation delay
                            newRow = CourseRow()
                        }
                    }
                }
            }
            else -> Unit
        }
    }
}
