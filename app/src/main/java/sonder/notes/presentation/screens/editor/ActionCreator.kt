package sonder.notes.presentation.screens.editor

import com.nigelbrown.fluxion.Flux
import com.nigelbrown.fluxion.FluxActionCreator
import sonder.notes.data.entities.Note
import sonder.notes.presentation.screens.editor.Store.Companion.UPDATE

class ActionCreator(flux: Flux): FluxActionCreator(flux) {
    fun update(note: Note) {
        emitAction(UPDATE, "note", note)
    }
}

