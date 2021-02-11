package com.example.keepnotes.ui.main

import androidx.annotation.VisibleForTesting
import androidx.lifecycle.Observer
import com.example.keepnotes.model.Note
import com.example.keepnotes.model.NoteResult
import com.example.keepnotes.model.NoteResult.Error
import com.example.keepnotes.model.Repository
import com.example.keepnotes.ui.base.BaseViewModel

class MainViewModel(val repository: Repository) :
    BaseViewModel<List<Note>?, MainViewState>() {

    private val repositoryNotes = repository.getNotes()
    private val notesObserver = object : Observer<NoteResult> {
        override fun onChanged(noteResult: NoteResult?) {
            noteResult?.let {
                when (it) {
                    is NoteResult.Success<*> -> {
                        viewStateLiveData.value =
                            MainViewState(notes = it.data as? List<Note>)
                    }
                    is Error -> {
                        viewStateLiveData.value = MainViewState(error = it.error)
                    }
                }
            }
        }
    }

    init {
        viewStateLiveData.value = MainViewState()
        repositoryNotes.observeForever(notesObserver)
    }

    @VisibleForTesting(otherwise = VisibleForTesting.PROTECTED)
    public override fun onCleared() {
        repositoryNotes.removeObserver(notesObserver)
    }

    fun deleteNote(noteId: String) {
        repository.deleteNote(noteId)
    }
}