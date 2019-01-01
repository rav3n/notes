package sonder.notes.presentation.screens.editor

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.EditorInfo
import android.view.inputmethod.InputMethodManager
import android.widget.TextView
import android.widget.Toast
import com.nigelbrown.fluxion.Annotation.React
import com.nigelbrown.fluxion.Reaction
import kotlinx.android.synthetic.main.fragment_editor.*
import sonder.notes.R
import sonder.notes.data.entities.Note
import sonder.notes.presentation.base.BaseFragment
import sonder.notes.presentation.screens.editor.Store.Companion.UPDATED

class EditorFragment : BaseFragment() {

    private lateinit var actionCreator: ActionCreator

    @React(reactionType = UPDATED)
    fun clean(reaction: Reaction) {
        Toast.makeText(requireContext(), getString(R.string.added), Toast.LENGTH_LONG).show()
        arguments!!.putLong(EditorFragment.javaClass.name, 0)
        edit_field.setText("")
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        actionCreator = ActionCreator(flux)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_editor, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        showKeyboard()
        val note = noteFromBundle()
        edit_field.setText(note.text)
        edit_field.setSelection(note.text.length)
        edit_field.setOnEditorActionListener(TextView.OnEditorActionListener { v, actionId, _ ->
            if (actionId == EditorInfo.IME_ACTION_DONE && v.text.isNotEmpty()) {
                actionCreator.update(Note(note.id, edit_field.text.toString()))
                return@OnEditorActionListener true
            }
            false
        })

        button_ready.setOnClickListener {
            if (edit_field.text.isNotEmpty()) {
                actionCreator.update(Note(note.id, edit_field.text.toString()))
            }
            hideKeyboard()
            root().popup()
        }
    }

    private fun showKeyboard() {
        edit_field.requestFocus()
        val imm = activity!!.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        imm.showSoftInput(edit_field, InputMethodManager.SHOW_IMPLICIT)
    }

    private fun hideKeyboard() {
        edit_field.clearFocus()
        val imm = activity!!.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        imm.hideSoftInputFromWindow(edit_field.windowToken, 0)
    }

    private fun noteFromBundle(): Note {
        val id = arguments!!.getLong(EditorFragment.javaClass.name, 0)
        val text = arguments!!.getString(EditorFragment.javaClass.canonicalName, "")
        return Note(id, text)
    }

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
}