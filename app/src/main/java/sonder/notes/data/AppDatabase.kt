package sonder.notes.data

import android.arch.persistence.room.Database
import android.arch.persistence.room.Room
import android.arch.persistence.room.RoomDatabase
import android.content.Context
import sonder.notes.data.dao.NoteDao
import sonder.notes.data.entities.Note

@Database(entities = [Note::class], version = 1)
abstract class AppDatabase : RoomDatabase() {

    abstract fun noteDao() : NoteDao

    companion object {
        private var INSTANCE: AppDatabase? = null
        @JvmStatic
        fun getInstance(context: Context): AppDatabase {
            synchronized(AppDatabase::class) {
                if (INSTANCE == null) {
                    INSTANCE = Room.databaseBuilder(context.applicationContext, AppDatabase::class.java, "note.db").build()
                }
            }
            return INSTANCE!!
        }
    }
}
