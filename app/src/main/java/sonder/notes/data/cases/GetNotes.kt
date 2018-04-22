package sonder.notes.data.cases

import sonder.notes.data.dao.NoteDao

class GetNotes(
    private val dao: NoteDao
) {
    fun all() = dao.all()
}