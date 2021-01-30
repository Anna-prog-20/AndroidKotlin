package com.example.keepnotes.ui.main

import com.example.keepnotes.model.Note

interface IRVOnItemClick {
    fun onItemClicked(note: Note?)
}