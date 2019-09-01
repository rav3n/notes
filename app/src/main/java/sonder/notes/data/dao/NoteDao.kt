package sonder.notes.data.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy.REPLACE
import androidx.room.Query
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