package com.example.golfstats.ui.Sessions

import com.example.golfstats.data.Course.CourseRow
import com.example.golfstats.data.Sessions.SessionRow


sealed interface SessionEvent {
    object OnAddNewClick: SessionEvent // bouton +
    object Dismiss: SessionEvent // cancel dans une création ou modification
    data class OnChangeddate(val date: String): SessionEvent // quand on écrit
    data class OnChangedtype(val type: String): SessionEvent // quand on écrit
    object SaveSession: SessionEvent // sauvegarder dans BD et potentiellement aller dans la session jouer
    object SaveSessionRange: SessionEvent // sauvegarder dans BD et potentiellement aller dans la session jouer
    data class Edit(val row: SessionRow): SessionEvent //bouton en crayon
    data class Delete(val row: SessionRow): SessionEvent //bouton en poubelle
    object OnAddNewCourseClick: SessionEvent
    object DismissCourse: SessionEvent
    data class OnChangednomCourse(val nom: String): SessionEvent
    data class OnChangeNumHoles(val num: Int): SessionEvent
    data class OnChangePar(val hole: Int, val num: Int): SessionEvent
    data class OnChangeYards(val hole: Int, val num: Int): SessionEvent
    object SaveCourse: SessionEvent
    data class PlayCourse(val course_id: Int): SessionEvent
    data class EditCourse(val row: CourseRow): SessionEvent
    data class DeleteCourse(val row: CourseRow): SessionEvent
    object Settings: SessionEvent
    data class SetSettings(val bool: Boolean): SessionEvent
    object SetSessionTypeCourse: SessionEvent
    data class CalculateScores(val session_id: Int): SessionEvent
    data class ResumeSession(val sessionRow: SessionRow): SessionEvent
    data class ResumeSessionRange(val sessionRow: SessionRow): SessionEvent
    object ExitCard: SessionEvent
    object OffSessionIdFound: SessionEvent
}