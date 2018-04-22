package sonder.notes.data.cases

import sonder.notes.data.dao.NoteDao
import sonder.notes.data.entities.NoteEntity

class InsertNote(
    private val dao: NoteDao
) {
    fun insert(entity: NoteEntity) { dao.insert(entity) }
}