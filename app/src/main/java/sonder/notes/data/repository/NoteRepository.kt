package sonder.notes.data.repository

import sonder.notes.data.AppDatabase
import sonder.notes.data.cases.DeleteNote
import sonder.notes.data.cases.GetNotes
import sonder.notes.data.cases.InsertNote
import sonder.notes.data.entities.NoteEntity

class NoteRepository(
    private val database: AppDatabase
) {
    fun list() = GetNotes(database.noteDao()).all()
    fun insert(entity: NoteEntity) = InsertNote(database.noteDao()).insert(entity)
    fun delete(id: Long) = DeleteNote(database.noteDao()).delete(id)
    fun deleteAll() = DeleteNote(database.noteDao()).deleteAll()
}