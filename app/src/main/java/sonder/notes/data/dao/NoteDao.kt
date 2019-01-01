package sonder.notes.data.dao

import android.arch.persistence.room.Dao
import android.arch.persistence.room.Delete
import android.arch.persistence.room.Insert
import android.arch.persistence.room.OnConflictStrategy.REPLACE
import android.arch.persistence.room.Query
import sonder.notes.data.entities.Note

@Dao
interface NoteDao {
    @Query("SELECT * from Note")
    fun all(): List<Note>

    @Insert(onConflict = REPLACE)
    fun insert(data: Note)

    @Query("DELETE from Note")
    fun deleteAll()

    @Delete
    fun delete(entity: Note)

    @Query("delete from Note where id like :id")
    fun deleteById(id: Long) : Int
}