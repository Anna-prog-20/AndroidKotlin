package com.example.keepnotes.ui.main

import androidx.annotation.VisibleForTesting
import androidx.lifecycle.Observer
import com.example.keepnotes.model.Note
import com.example.keepnotes.model.NoteResult
import com.example.keepnotes.model.NoteResult.Error
import com.example.keepnotes.model.Repository
import com.example.keepnotes.ui.base.BaseViewModel
import com.example.keepnotes.ui.note.NoteViewState
import kotlinx.coroutines.channels.consumeEach
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking

class MainViewModel(val repository: Repository) :
    BaseViewModel<List<Note>?>() {

    private val notesChannel by lazy { runBlocking { repository.getNotes() } }

    init {
        launch {
            notesChannel.consumeEach { noteResult ->
                when (noteResult) {
                    is NoteResult.Success<*> -> setData(noteResult.data as? List<Note>)
                    is NoteResult.Error -> setError(noteResult.error)
                }
            }
        }
    }

    fun deleteNote(noteId: String) {
        launch {
            try {
                repository.deleteNote(noteId)
            } catch (e: Throwable) {
                setError(e)
            }

        }
    }

    @VisibleForTesting(otherwise = VisibleForTesting.PROTECTED)
    public override fun onCleared() {
        notesChannel.cancel()
        super.onCleared()
    }
}