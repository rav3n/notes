package sonder.notes.presentation

import android.app.Application
import sonder.notes.data.AppDatabase

class NoteApplication : Application() {
    val db: AppDatabase by lazy { AppDatabase.getInstance(this) }
}