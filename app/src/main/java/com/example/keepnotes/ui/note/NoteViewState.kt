package com.example.keepnotes.ui.note

import com.example.keepnotes.model.Note
import com.example.keepnotes.ui.base.BaseViewState

class NoteViewState(note: Note? = null, error: Throwable? = null) :
    BaseViewState<Note?>(note, error)