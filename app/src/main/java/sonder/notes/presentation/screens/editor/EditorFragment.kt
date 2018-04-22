package sonder.notes.presentation.screens.editor

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.EditorInfo
import android.view.inputmethod.InputMethodManager
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
            bundle.putLong(EditorFragment.javaClass.name, id)
            fragment.arguments = bundle
            return fragment
        }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        binding = FragmentEditorBinding.inflate(inflater, container, false)
        binding.note = noteFromBundle()
        binding.editField.setOnEditorActionListener(TextView.OnEditorActionListener { v, actionId, _ ->
            if (actionId == EditorInfo.IME_ACTION_DONE && v.text.isNotEmpty()) {
                hideKeyboard()
                handle(binding.note!!.id, v.text.toString())
                root().popup()
                return@OnEditorActionListener true
            }
            false
        })
        return binding.root
    }

    private fun handle(id: Long, text: String) {
        if (id > 0) {
            notesViewModel().update(id, text)
        } else {
            notesViewModel().create(text)
        }
    }

    private fun noteFromBundle(): Note {
        val id = arguments!!.getLong(EditorFragment.javaClass.name, 0)
        val text = arguments!!.getString(EditorFragment.javaClass.canonicalName, "")
        return Note(id, text)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        showKeyboard()
    }

    private fun showKeyboard() {
        binding.editField.requestFocus()
        val imm = activity!!.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        imm.showSoftInput(binding.editField, InputMethodManager.SHOW_IMPLICIT)
    }

    private fun hideKeyboard() {
        binding.editField.clearFocus()
        val imm = activity!!.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        imm.hideSoftInputFromWindow(binding.editField.windowToken, 0)
    }

}