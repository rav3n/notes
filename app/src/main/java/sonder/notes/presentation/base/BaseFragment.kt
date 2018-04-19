package sonder.notes.presentation.base

import android.support.v4.app.Fragment

abstract class BaseFragment : Fragment() {
    fun root() = activity as ApplicationActivity
}