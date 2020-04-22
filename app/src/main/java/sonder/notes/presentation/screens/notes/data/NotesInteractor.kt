package sonder.notes.presentation.screens.notes.data

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.nigelbrown.fluxion.Flux
import kotlinx.coroutines.experimental.CommonPool
import kotlinx.coroutines.experimental.android.UI
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
        launch(CommonPool) {
            noteRepository.insert(note)
            fetchData()
        }
    }

    fun delete(id: Long) {
        launch(CommonPool) {
            noteRepository.delete(id)
            fetchData()
        }
    }

    fun deleteAll() {
        launch(CommonPool) {
            noteRepository.deleteAll()
            fetchData()
        }
    }

    fun fetchData() {
        launch(CommonPool) {
            notes.postValue(mapper(noteRepository.list()))
        }
    }

    private fun mapper(source: List<Note>) : ArrayList<Note> {
        val result = arrayListOf<Note>()
        source.forEach{result.add(Note(it.id, it.text))}
        return result
    }
}