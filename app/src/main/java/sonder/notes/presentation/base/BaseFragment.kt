package sonder.notes.presentation.base

import android.arch.lifecycle.ViewModelProviders
import android.support.v4.app.Fragment
import sonder.notes.presentation.screens.notes.data.NotesViewModel

abstract class BaseFragment : Fragment() {
    fun root() = activity as ApplicationActivity
    fun notesViewModel() = ViewModelProviders.of(root()).get(NotesViewModel::class.java)
}