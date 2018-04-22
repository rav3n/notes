package sonder.notes.data.cases

import sonder.notes.data.dao.NoteDao
import sonder.notes.data.entities.NoteEntity

class DeleteNote(
    private val dao: NoteDao
) {
    fun delete(entity: NoteEntity) { dao.delete(entity) }
    fun delete(id: Long) { dao.deleteById(id) }
    fun deleteAll() { dao.deleteAll() }
}