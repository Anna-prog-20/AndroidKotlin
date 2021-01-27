package com.example.keepnotes.ui.main

import com.example.keepnotes.model.Note
import com.example.keepnotes.ui.base.BaseViewState

class MainViewState(notes: List<Note>? = null, error: Throwable? = null) :
    BaseViewState<List<Note>?>(notes, error)