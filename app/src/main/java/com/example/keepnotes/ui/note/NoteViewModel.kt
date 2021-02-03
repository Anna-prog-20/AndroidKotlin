package com.example.keepnotes.ui.note

import androidx.lifecycle.Observer
import com.example.keepnotes.model.Note
import com.example.keepnotes.model.NoteResult
import com.example.keepnotes.model.NoteResult.Error
import com.example.keepnotes.model.Repository
import com.example.keepnotes.ui.base.BaseViewModel

class NoteViewModel(val repository: Repository) :
    BaseViewModel<NoteViewState.Data, NoteViewState>() {
    private val currentNote: Note?
        get() = viewStateLiveData.value?.data?.note

    fun saveChanges(note: Note) {
        viewStateLiveData.value = NoteViewState(NoteViewState.Data(note = note))
    }

    override fun onCleared() {
        currentNote?.let { repository.saveNote(it) }
    }

    fun loadNote(noteId: String) {
        repository.getNoteById(noteId).observeForever { t ->
            t?.let { noteResult ->
                viewStateLiveData.value = when (noteResult) {
                    is NoteResult.Success<*> ->
                        NoteViewState(NoteViewState.Data(note = noteResult.data as? Note))
                    is Error ->
                        NoteViewState(error = noteResult.error)
                }
            }
        }
    }

    fun deleteNote() {
        currentNote?.let { currentNote ->
            repository.deleteNote(currentNote.id).observeForever { result ->
                result?.let { noteResult ->
                    viewStateLiveData.value = when (noteResult) {
                        is NoteResult.Success<*> ->
                            NoteViewState(NoteViewState.Data(isDeleted = true))
                        is Error ->
                            NoteViewState(error = noteResult.error)
                    }
                }
            }
        }
    }

}