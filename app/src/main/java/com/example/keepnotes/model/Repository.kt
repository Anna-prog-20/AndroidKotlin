package com.example.keepnotes.model

import androidx.lifecycle.MutableLiveData

class Repository(private val remoteProvider: RemoteDataProvider) {
    private val notesLiveData = MutableLiveData<List<Note>>()
    private var notes: MutableList<Note> = mutableListOf()

    init {
        notesLiveData.value = notes
    }

    fun setNotes(listNotes: MutableList<Note>) {
        notes = listNotes
        notesLiveData.value = notes
    }

    fun getNotes() = remoteProvider.subscribeToAllNotes()
    fun saveNote(note: Note) = remoteProvider.saveNote(note)
    fun deleteNote(noteId: String) = remoteProvider.deleteNote(noteId)
    fun getNoteById(id: String) = remoteProvider.getNoteById(id)
    fun getCurrentUser() = remoteProvider.getCurrentUser()

}