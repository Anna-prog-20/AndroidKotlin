package com.example.keepnotes.model

import androidx.lifecycle.MutableLiveData

class Repository(private val remoteProvider: RemoteDataProvider) {
    private val notesLiveData = MutableLiveData<List<Note>>()

    suspend fun getNotes() = remoteProvider.subscribeToAllNotes()
    suspend fun saveNote(note: Note) = remoteProvider.saveNote(note)
    suspend fun deleteNote(noteId: String) = remoteProvider.deleteNote(noteId)
    suspend fun getNoteById(id: String) = remoteProvider.getNoteById(id)
    suspend fun getCurrentUser() = remoteProvider.getCurrentUser()

}