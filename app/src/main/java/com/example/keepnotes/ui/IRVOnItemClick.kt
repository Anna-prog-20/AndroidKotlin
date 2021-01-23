package com.example.keepnotes.ui

import com.example.keepnotes.model.Note

interface IRVOnItemClick {
    fun onItemClicked(note: Note?)
}