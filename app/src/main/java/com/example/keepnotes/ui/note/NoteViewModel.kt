package com.example.keepnotes.ui.note

import androidx.lifecycle.ViewModel
import com.example.keepnotes.model.Note
import com.example.keepnotes.model.Repository

class NoteViewModel(private val repository: Repository = Repository) : ViewModel() {
    private var pendingNote: Note? = null

    fun saveChanges(note: Note) {
        pendingNote = note
    }

    override fun onCleared() {
        if (pendingNote != null) {
            repository.saveNote(pendingNote!!)
        }
    }
}