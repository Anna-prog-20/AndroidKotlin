package com.example.keepnotes.ui.main

import androidx.lifecycle.Observer
import com.example.keepnotes.model.Note
import com.example.keepnotes.model.NoteResult
import com.example.keepnotes.model.NoteResult.Error
import com.example.keepnotes.model.Repository
import com.example.keepnotes.ui.base.BaseViewModel

class MainViewModel(private val repository: Repository = Repository) :
    BaseViewModel<List<Note>?, MainViewState>() {

    private val repositoryNotes = repository.getNotes()
    private val notesObserver = object : Observer<NoteResult> {
        override fun onChanged(noteResult: NoteResult?) {
            noteResult?.let {noteResult ->
                when (noteResult) {
                    is NoteResult.Success<*> -> {
                        viewStateLiveData.value =
                            MainViewState(notes = noteResult.data as? List<Note>)
                    }
                    is Error -> {
                        viewStateLiveData.value = MainViewState(error = noteResult.error)
                    }
                }
            }
                ?: return
        }

    }

    init {
        viewStateLiveData.value = MainViewState()
        repositoryNotes.observeForever(notesObserver)
    }

    override fun onCleared() {
        repositoryNotes.removeObserver(notesObserver)
    }

    fun deleteNote(note: Note) {
        repository.deleteNote(note)
    }
}