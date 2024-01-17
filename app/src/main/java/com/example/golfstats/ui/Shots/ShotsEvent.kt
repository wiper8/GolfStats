package com.example.golfstats.ui.Shots

import com.example.golfstats.data.Shots.ShotRow
import com.example.golfstats.data.ShotsAvailable.ShotsAvailableRow

sealed interface ShotEvent {
    object SETDEFAULT: ShotEvent
    object onAddShot: ShotEvent
    object DismissShot: ShotEvent
    object DismissShotAvailable: ShotEvent
    data class OnChooseShot(val shot: String): ShotEvent
    data class OnChangedsucess(val type: Int): ShotEvent
    data class OnChangedpenalty(val type: Int): ShotEvent
    data class OnChangedgreen(val type: Int): ShotEvent
    data class OnChangedreset(val type: Int): ShotEvent
    data class OnChangedputt(val type: Int): ShotEvent
    object SaveCurrentShot: ShotEvent
    object OnEditShotAvailable: ShotEvent
    data class onEditExistingShotAvailable(val shot: ShotsAvailableRow): ShotEvent
    data class onChangeShotAvailableName(val shot: String): ShotEvent
    data class onChangeShotAvailableID(val id: Int): ShotEvent
    object SaveShotAvailable: ShotEvent
    object OnAddNewShotAvailable: ShotEvent
    data class DeleteShotAvailable(val shotavailable: ShotsAvailableRow): ShotEvent
    data class SetSessionId(val session_id: Int): ShotEvent
    data class ClickButtonGreen(val bool: Boolean): ShotEvent
    data class ClickButtonPenalty(val bool: Boolean): ShotEvent
    data class ClickButtonReset(val bool: Boolean): ShotEvent
    object NewAvailableShotIsPutt: ShotEvent
    data class DeleteRecordedShot(val shot: ShotRow): ShotEvent
    data class SetHoleNum(val hole_num: Int): ShotEvent
    data class SetCourseId(val id: Int?): ShotEvent
}