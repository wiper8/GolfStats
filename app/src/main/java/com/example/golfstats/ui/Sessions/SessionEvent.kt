package com.example.golfstats.ui.Sessions

import com.example.golfstats.data.Sessions.SessionRow


sealed interface SessionEvent {
    object OnAddNewClick: SessionEvent // bouton +
    object Dismiss: SessionEvent // cancel dans une création ou modification
    data class OnChangeddate(val date: String): SessionEvent // quand on écrit
    data class OnChangedtype(val type: String): SessionEvent // quand on écrit
    object Save: SessionEvent // sauvegarder dans BD et potentiellement aller dans la session jouer
    data class Edit(val row: SessionRow): SessionEvent //bouton en crayon
    data class Delete(val row: SessionRow): SessionEvent //bouton en poubelle
}