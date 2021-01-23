package com.example.keepnotes.model

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData

object Repository {
    private val notesLiveData = MutableLiveData<List<Note>>()

    private var notes: MutableList<Note> = mutableListOf()

    init {
        notesLiveData.value = notes
    }

    fun getNotes(): LiveData<List<Note>> = notesLiveData

    fun setNotes(listNotes: MutableList<Note>) {
        notes = listNotes
        notesLiveData.value = notes
    }

    fun saveNote(note: Note) {
        addOrReplace(note)
        notesLiveData.value = notes
    }

    private fun addOrReplace(note: Note) {
        for (i in 0 until notes.size) {
            if (notes[i] == note) {
                notes[i] = note
                return
            }
        }
        notes.add(note)
    }

}