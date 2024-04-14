package com.example.golfstats.ui.Recommendations

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.golfstats.data.Course.CourseRepo
import com.example.golfstats.data.Holes.HoleRow
import com.example.golfstats.data.Holes.HolesRepo
import com.example.golfstats.data.Recommendations.RecommendationRow
import com.example.golfstats.data.Recommendations.RecommendationsRepo
import com.example.golfstats.data.ShotsAvailable.ShotsAvailableRepo
import com.example.golfstats.data.ShotsAvailable.ShotsAvailableRow
import kotlinx.coroutines.cancel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import java.lang.Math.floor

class RecommendationsViewModel(val holesRepo: HolesRepo,
                               val shotavailableRepo: ShotsAvailableRepo,
                               val recommendationsRepo: RecommendationsRepo) : ViewModel() {

    private val _state = MutableStateFlow(RecommendationsState())

    val state = combine(
        _state,
        holesRepo.getHoles(),
        shotavailableRepo.getShots(),
        recommendationsRepo.getRecommendations(),
        recommendationsRepo.getUniqueRecommendationsID()
    ) { state, allHoles, allShotsAvailable, allRecommendations, allRecommId ->
        state.copy(
            allHoles = allHoles,
            allShotsAvailable = allShotsAvailable,
            allRecommendations = allRecommendations,
            allRecommendationsID = allRecommId
        )
    }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000L), RecommendationsState())

    var newExpect: RecommendationRow by mutableStateOf(RecommendationRow())
        private set

    var newRecommendations = mutableStateListOf<RecommendationRow>()
        private set

    private fun validatedigitsint(expect: String): Boolean {
        return !expect.any { char -> char !in listOf<Char>('0', '1', '2', '3', '4', '5', '6', '7', '8', '9') } && expect.length > 0
    }
    private fun validatedigitsfloat(expect: String): Boolean {
        if(expect == "") return true
        if(expect.substring(0, 1) == "0") return false
        if(expect.length > 1) return false
        return !expect.any { char -> char !in listOf<Char>('0', '1', '2', '3', '4', '5', '6', '7', '8', '9') }
    }

    private fun validateExpectInt(expect: String): Int {
        if(expect == "0") return 0
        var i=0
        var tmp_expect = expect

        while (true) {
            if(tmp_expect.substring(i, i+1) == "0") {
                tmp_expect = tmp_expect.substring(i+1, tmp_expect.length) //TODO vérifier
            } else {
                return tmp_expect.toInt()
            }
        }
        return tmp_expect.toInt()
    }

    private fun validateExpectFloat(expect: String): Double {
        if(expect == "0" || expect == "") return 0.0
        return ("0."+expect).toDouble()
    }

    private fun validateRecommendation(l: List<RecommendationRow>): Boolean {
        l.forEachIndexed { i, e ->
            if (e.num == 0 || e.recommend_id == 0) {
                return false
            }
            l.forEachIndexed { j, e2 ->
                if(i!=j) {
                    if(l[i].num == l[j].num) return false
                }
            }
        }
        return true
    }

    fun onEvent(event: RecommendationsEvent) {
        when (event) {
            RecommendationsEvent.OnAddNewClick -> {
                _state.update {
                    it.copy(
                        is_choix_open = true
                    )
                }
            }
            RecommendationsEvent.DismissRecommend -> {
                 _state.update {
                        it.copy(
                            is_choix_open = false
                        )
                    }
                newExpect = RecommendationRow()
                newRecommendations.clear()
            }
            RecommendationsEvent.SaveRecommendation -> {
                if(newRecommendations.size > 0) {
                    val new_recomm_ID: Int

                    //trouver quel recomm_ID (nombre déjà existant pour ce trou + 1)
                    if (_state.value.allRecommendationsID.size == 0) {
                        new_recomm_ID = 1
                    } else {
                        new_recomm_ID = _state.value.allRecommendationsID.max() + 1
                    }
                    //remplacer les recomm_ID
                    for (i in 1..newRecommendations.size) {
                        newRecommendations[i - 1].recommend_id = new_recomm_ID
                        newRecommendations[i - 1].expectency = newExpect.expectency
                    }

                    //S'assurer de la validiter de toutes les recommendations
                    if (validateRecommendation(newRecommendations)) {
                        viewModelScope.launch {
                            newRecommendations.forEachIndexed { i, e ->
                                recommendationsRepo.upsert(e)
                            }
                            _state.update {
                                it.copy(
                                    is_choix_open = false
                                )
                            }
                            newExpect = RecommendationRow()
                            newRecommendations.clear()
                        }
                    }
                }
            }

            is RecommendationsEvent.SetHoleId -> {
                _state.update {
                    it.copy(
                        hole_id = event.id
                    )
                }
                viewModelScope.launch {
                    recommendationsRepo.getUniqueRecommendationsHole(event.id).collect { l->
                        _state.update {
                            it.copy(
                                allRecommendationsID = l
                            )
                        }
                    }
                }
            }
            is RecommendationsEvent.SetHoleNum -> {
                _state.update {
                    it.copy(
                        hole_num = event.num
                    )
                }
            }

            is RecommendationsEvent.OnClickShot -> {
                val newShot = RecommendationRow(
                    recommend_id = 0,
                    num = 1+newRecommendations.size,
                    shot_id = event.shot.id,
                    hole_id = _state.value.hole_id,
                    expectency = 0.0 //TODO vérifier si c'est vraiment changé dans OnChangeExpectInt et OnChangeExpectFloat
                )
                newRecommendations.add(newShot)
            }

            is RecommendationsEvent.OnChangeExpectInt -> {
                if(validatedigitsint(event.expect)) {
                    newExpect = newExpect.copy(
                        expectency = validateExpectInt(event.expect) + newExpect.expectency - floor(newExpect.expectency)
                    )
                }

            }
            is RecommendationsEvent.OnChangeExpectFloat -> {
                if(validatedigitsfloat(event.expect)) {
                    newExpect = newExpect.copy(
                        expectency = validateExpectFloat(event.expect) + floor(newExpect.expectency)
                    )
                }
            }
            is RecommendationsEvent.DeleteRecommend -> {
                viewModelScope.launch {
                    for (recomm in event.l) {
                        recommendationsRepo.delete(recomm)
                    }
                }
            }
        }
    }
}
