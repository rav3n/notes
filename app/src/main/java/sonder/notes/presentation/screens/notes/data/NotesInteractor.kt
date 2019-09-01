package sonder.notes.presentation.screens.notes.data

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.nigelbrown.fluxion.Flux
import kotlinx.coroutines.experimental.CommonPool
import kotlinx.coroutines.experimental.android.UI
import kotlinx.coroutines.experimental.async
import kotlinx.coroutines.experimental.launch
import sonder.notes.data.entities.Note
import sonder.notes.data.repository.NoteRepository
import sonder.notes.presentation.screens.editor.Store

class NotesInteractor(
    private val noteRepository: NoteRepository,
    private val flux: Flux
) : ViewModel() {

    val notes = MutableLiveData<ArrayList<Note>>()

    private var store: Store = Store(flux, this)

    fun create(title: String) { updateOrCreate(Note(0, title)) }
    fun update(note: Note) { updateOrCreate(note) }
    private fun updateOrCreate(note: Note) {
        launch(UI) {
            async(CommonPool) { noteRepository.insert(note) }.await()
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

    private fun mapper(source: List<Note>) : ArrayList<Note> {
        val result = arrayListOf<Note>()
        source.forEach{result.add(Note(it.id, it.text))}
        return result
    }
}