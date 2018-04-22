package sonder.notes.presentation.screens.notes.actions

import sonder.notes.presentation.screens.notes.data.entity.Note

interface RecyclerActions {
    fun onEditAction(note: Note)
}