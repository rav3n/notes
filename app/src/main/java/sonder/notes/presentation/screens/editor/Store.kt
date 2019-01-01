package sonder.notes.presentation.screens.editor

import com.nigelbrown.fluxion.Annotation.Action
import com.nigelbrown.fluxion.Annotation.Store
import com.nigelbrown.fluxion.Flux
import com.nigelbrown.fluxion.FluxAction
import com.nigelbrown.fluxion.FluxStore
import sonder.notes.data.entities.Note
import sonder.notes.presentation.screens.notes.data.NotesInteractor

@Store
class Store(flux: Flux, private val interactor: NotesInteractor): FluxStore(flux) {
    init {
        registerActionSubscriber(this)
    }
    @Action(actionType = UPDATE)
    fun update(action: FluxAction) {
        val note = action.data["note"] as Note
        interactor.update(note)
        emitReaction(UPDATED)
    }

    companion object {
        const val UPDATE = "action_update"
        const val UPDATED = "reaction_updated"
    }
}
