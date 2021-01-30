package com.example.keepnotes.model

import androidx.lifecycle.MutableLiveData

object Repository {
    private val notesLiveData = MutableLiveData<List<Note>>()
    private var notes: MutableList<Note> = mutableListOf()
    private val remoteProvider: RemoteDataProvider = FireStoreProvider()

    init {
        notesLiveData.value = notes
    }

    fun setNotes(listNotes: MutableList<Note>) {
        notes = listNotes
        notesLiveData.value = notes
    }

    fun getNotes() = remoteProvider.subscribeToAllNotes()
    fun saveNote(note: Note) = remoteProvider.saveNote(note)
    fun deleteNote(note: Note) = remoteProvider.deleteNote(note)
    fun getNoteById(id: String) = remoteProvider.getNoteById(id)
    fun getCurrentUser() = remoteProvider.getCurrentUser()

}