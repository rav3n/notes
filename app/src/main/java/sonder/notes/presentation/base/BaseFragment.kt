package sonder.notes.presentation.base

import android.arch.lifecycle.ViewModel
import android.arch.lifecycle.ViewModelProvider
import android.arch.lifecycle.ViewModelProviders
import android.support.v4.app.Fragment
import sonder.notes.data.repository.NoteRepository
import sonder.notes.presentation.NoteApplication
import sonder.notes.presentation.screens.notes.data.NotesViewModel

abstract class BaseFragment : Fragment() {

    fun root() = activity as ApplicationActivity
    fun db() = (root().application as NoteApplication).db

    fun notesViewModel() = ViewModelProviders.of(root(), object : ViewModelProvider.Factory {
        override fun <T : ViewModel?> create(modelClass: Class<T>): T {
            val noteRepository = NoteRepository(db())
            return NotesViewModel(noteRepository) as T
        }
    }).get(NotesViewModel::class.java)
}