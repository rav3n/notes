package sonder.notes.presentation.base

import android.arch.lifecycle.ViewModel
import android.arch.lifecycle.ViewModelProvider
import android.arch.lifecycle.ViewModelProviders
import android.os.Bundle
import android.support.v4.app.Fragment
import com.nigelbrown.fluxion.Flux
import sonder.notes.data.repository.NoteRepository
import sonder.notes.presentation.NoteApplication
import sonder.notes.presentation.screens.notes.data.NotesInteractor

abstract class BaseFragment : Fragment() {

    fun root() = activity as ApplicationActivity
    fun db() = (root().application as NoteApplication).db

    protected lateinit var flux: Flux

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        flux = Flux.init(requireActivity().application)
        flux.registerReactionSubscriber(this)
    }

    override fun onDestroy() {
        flux.unregesterReactionSubscriber(this)
        super.onDestroy()
    }

    fun notesViewModel() = ViewModelProviders.of(root(), object : ViewModelProvider.Factory {
        override fun <T : ViewModel?> create(modelClass: Class<T>): T {
            val noteRepository = NoteRepository(db())
            return NotesInteractor(noteRepository, flux) as T
        }
    }).get(NotesInteractor::class.java)
}