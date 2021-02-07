package com.example.keepnotes.ui.note

import androidx.lifecycle.Observer
import com.example.keepnotes.model.Note
import com.example.keepnotes.model.NoteResult
import com.example.keepnotes.model.NoteResult.Error
import com.example.keepnotes.model.Repository
import com.example.keepnotes.ui.base.BaseViewModel

class NoteViewModel(private val repository: Repository = Repository) :
    BaseViewModel<Note?, NoteViewState>() {
    private var pendingNote: Note? = null

    fun saveChanges(note: Note) {
        pendingNote = note
    }

    override fun onCleared() {
        if (pendingNote != null) {
            repository.saveNote(pendingNote!!)
        }
    }

    fun loadNote(noteId: String) {
        repository.getNoteById(noteId).observeForever(object : Observer<NoteResult> {
            override fun onChanged(noteResult: NoteResult?) {
                if (noteResult == null) return

                when (noteResult) {
                    is NoteResult.Success<*> ->
                        viewStateLiveData.value = NoteViewState(note = noteResult.data as? Note)
                    is Error ->
                        viewStateLiveData.value = NoteViewState(error = noteResult.error)
                }
            }
        })
    }

}