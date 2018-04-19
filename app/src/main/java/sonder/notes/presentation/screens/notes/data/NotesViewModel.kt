package sonder.notes.presentation.screens.notes.data

import android.arch.lifecycle.MutableLiveData
import android.arch.lifecycle.ViewModel
import sonder.notes.presentation.screens.notes.data.entity.Note

class NotesViewModel : ViewModel() {
    val notes = MutableLiveData<ArrayList<Note>>()
    fun updateOrCreate(id: Long, title: String) {
        if (id > 0) {
            val note = notes.value!!.findLast { it.id == id }
            if (note != null) {}
        } else {
            if (notes.value != null) {
                notes.value!!.add(Note(notes.value!!.size.toLong(), title))
                notes.postValue(notes.value)
            } else {
                val arr = ArrayList<Note>()
                arr.add(Note(1, title))
                notes.postValue(arr)
            }
        }
    }
}