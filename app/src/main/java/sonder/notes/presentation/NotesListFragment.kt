package sonder.notes.presentation

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import sonder.notes.databinding.FragmentNotesListBinding

class NotesListFragment : BaseFragment() {

    private lateinit var binding: FragmentNotesListBinding

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        binding = FragmentNotesListBinding.inflate(inflater, container, false)
        binding.recycler.
        return binding.root
    }

    companion object {
        @JvmStatic fun newInstance() = NotesListFragment()
    }
}