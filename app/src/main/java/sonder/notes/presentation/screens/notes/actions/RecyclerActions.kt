package sonder.notes.presentation.screens.notes.actions

import sonder.notes.data.entities.Note

interface RecyclerActions {
    fun onEditAction(note: Note)
}