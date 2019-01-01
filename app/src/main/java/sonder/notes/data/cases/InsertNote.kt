package sonder.notes.data.cases

import sonder.notes.data.dao.NoteDao
import sonder.notes.data.entities.Note

class InsertNote(
    private val dao: NoteDao
) {
    fun insert(entity: Note) { dao.insert(entity) }
}