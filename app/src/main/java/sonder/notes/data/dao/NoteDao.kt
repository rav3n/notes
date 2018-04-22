package sonder.notes.data.dao

import android.arch.persistence.room.Dao
import android.arch.persistence.room.Delete
import android.arch.persistence.room.Insert
import android.arch.persistence.room.OnConflictStrategy.REPLACE
import android.arch.persistence.room.Query
import sonder.notes.data.entities.NoteEntity

@Dao
interface NoteDao {
    @Query("SELECT * from NoteEntity")
    fun all(): List<NoteEntity>

    @Insert(onConflict = REPLACE)
    fun insert(data: NoteEntity)

    @Query("DELETE from NoteEntity")
    fun deleteAll()

    @Delete
    fun delete(entity: NoteEntity)

    @Query("delete from NoteEntity where id like :id")
    fun deleteById(id: Long) : Int
}