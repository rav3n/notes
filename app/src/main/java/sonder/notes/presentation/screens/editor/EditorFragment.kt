package sonder.notes.presentation.screens.editor

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.EditorInfo
import android.widget.TextView
import sonder.notes.databinding.FragmentEditorBinding
import sonder.notes.presentation.base.BaseFragment
import sonder.notes.presentation.screens.editor.data.Note

class EditorFragment : BaseFragment() {

    private lateinit var binding: FragmentEditorBinding

    companion object {
        fun newInstance(id:Long, source: String): BaseFragment {
            val fragment = EditorFragment()
            val bundle = Bundle()
            bundle.putString(EditorFragment.javaClass.canonicalName, source)
            fragment.arguments = bundle
            return fragment
        }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        binding = FragmentEditorBinding.inflate(inflater, container, false)
        binding.note = Note()
        binding.editField.setOnEditorActionListener(TextView.OnEditorActionListener { v, actionId, event ->
            if (actionId == EditorInfo.IME_ACTION_DONE) {
                notesViewModel().updateOrCreate(0, v!!.text.toString())
                root().popup()
                return@OnEditorActionListener true
            }
            false
        })
        return binding.root
    }

}