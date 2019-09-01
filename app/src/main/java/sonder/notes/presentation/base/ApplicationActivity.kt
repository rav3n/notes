package sonder.notes.presentation.base

import android.os.Bundle
import androidx.fragment.app.Fragment
import androidx.appcompat.app.AppCompatActivity
import sonder.notes.R
import sonder.notes.presentation.screens.notes.NotesListFragment

class ApplicationActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_application)
        firstStart(savedInstanceState) {
            pushFragment(NotesListFragment.newInstance(), false)
        }
    }

    private fun firstStart(savedInstanceState: Bundle?, func: () -> Unit) {
        if (savedInstanceState == null) {
            func.invoke()
        }
    }

    fun pushFragment(fragment: Fragment, addToBackStack: Boolean = true) {
        supportFragmentManager.beginTransaction()
            .replace(R.id.container, fragment)
            .apply {
                if (addToBackStack) {
                    addToBackStack(fragment.toString())
                }
            }
            .commit()
    }

    fun popup() {
        supportFragmentManager.popBackStack()
    }
}