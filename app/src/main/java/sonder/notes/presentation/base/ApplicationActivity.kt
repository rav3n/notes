package sonder.notes.presentation.base

import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v7.app.AppCompatActivity
import sonder.notes.R
import sonder.notes.presentation.screens.notes.NotesListFragment

class ApplicationActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_application)
        firstStart(savedInstanceState) {
            pushFragment(NotesListFragment.newInstance())
        }
    }

    private fun firstStart(savedInstanceState: Bundle?, func: () -> Unit) {
        if (savedInstanceState == null) {
            func.invoke()
        }
    }

    fun pushFragment(fragment: Fragment) {
        supportFragmentManager.beginTransaction()
            .replace(R.id.container, fragment)
            .addToBackStack(fragment.toString())
            .commit()
    }

    fun popup() {
        supportFragmentManager.popBackStack()
    }
}