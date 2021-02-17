package com.example.keepnotes.model

import androidx.lifecycle.MutableLiveData

class Repository(private val remoteProvider: RemoteDataProvider) {
    //private val notesLiveData = MutableLiveData<List<Note>>()

    fun getNotes() = remoteProvider.subscribeToAllNotes()
    fun saveNote(note: Note) = remoteProvider.saveNote(note)
    fun deleteNote(noteId: String) = remoteProvider.deleteNote(noteId)
    fun getNoteById(id: String) = remoteProvider.getNoteById(id)
    fun getCurrentUser() = remoteProvider.getCurrentUser()

}