package com.example.keepnotes.ui.note

import com.example.keepnotes.model.Note
import com.example.keepnotes.model.NoteResult
import com.example.keepnotes.model.NoteResult.Error
import com.example.keepnotes.model.Repository
import com.example.keepnotes.ui.base.BaseViewModel
import kotlinx.coroutines.launch

class NoteViewModel(val repository: Repository) :
    BaseViewModel<NoteViewState.Data>() {
    private val currentNote: Note?
        get() = getViewState().poll()?.note

    fun saveChanges(note: Note) {
        setData(NoteViewState.Data(note = note))
    }

    fun loadNote(noteId: String) {
        launch {
            try {
                setData(NoteViewState.Data(note = repository.getNoteById(noteId)))
            } catch (e: Throwable) {
                setError(e)
            }
        }
    }

    fun deleteNote() {
        launch {
            try {
                currentNote?.let { repository.deleteNote(it.id) }
                setData(NoteViewState.Data(isDeleted = true))
            } catch (e: Throwable) {
                setError(e)
            }
        }
    }

    override fun onCleared() {
        launch {
            currentNote?.let { note ->
                repository.saveNote(note)
            }
            super.onCleared()
        }
    }

}