package sonder.notes.presentation.screens.notes.data

import android.arch.lifecycle.MutableLiveData
import android.arch.lifecycle.ViewModel
import kotlinx.coroutines.experimental.CommonPool
import kotlinx.coroutines.experimental.android.UI
import kotlinx.coroutines.experimental.async
import kotlinx.coroutines.experimental.launch
import sonder.notes.data.entities.NoteEntity
import sonder.notes.data.repository.NoteRepository
import sonder.notes.presentation.screens.notes.data.entity.Note

class NotesViewModel(
    private val noteRepository: NoteRepository
) : ViewModel() {
    val notes = MutableLiveData<ArrayList<Note>>()

    fun create(title: String) { updateOrCreate(0, title) }
    fun update(id:Long, title: String) { updateOrCreate(id, title) }
    private fun updateOrCreate(id: Long, title: String) {
        launch(UI) {
            async(CommonPool) { noteRepository.insert(NoteEntity(id, title)) }.await()
            fetchData()
        }
    }

    fun delete(id: Long) {
        launch(UI) {
            async(CommonPool) { noteRepository.delete(id) }.await()
            fetchData()
        }
    }

    fun deleteAll() {
        launch(UI) {
            async(CommonPool) { noteRepository.deleteAll() }.await()
            fetchData()
        }
    }

    fun fetchData() {
        launch(UI) { async(CommonPool) { notes.postValue(mapper(noteRepository.list())) }.await() }
    }

    private fun mapper(source: List<NoteEntity>) : ArrayList<Note> {
        val result = arrayListOf<Note>()
        source.forEach{result.add(Note(it.id, it.text))}
        return result
    }
}