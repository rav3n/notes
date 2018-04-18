package sonder.notes.presentation

import android.databinding.DataBindingUtil
import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v7.app.AppCompatActivity
import sonder.notes.R
import sonder.notes.databinding.ActivityApplicationBinding

class ApplicationActivity : AppCompatActivity() {

    private lateinit var binding: ActivityApplicationBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_application)

        if (isFirstStart(savedInstanceState)) {
            coldStart()
        }
    }

    private fun coldStart() {
        pushFragment(NotesListFragment.newInstance(), false)
    }

    private fun isFirstStart(savedInstanceState: Bundle?) = savedInstanceState == null

    fun pushFragment(fragment: Fragment, add: Boolean) {
        val transaction = supportFragmentManager.beginTransaction()
        transaction.replace(R.id.container, fragment)
            .commit()
        if (add) transaction.addToBackStack(fragment.toString())
        supportFragmentManager.executePendingTransactions()

    }
}