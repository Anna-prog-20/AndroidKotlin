package com.example.keepnotes.ui.note

import com.example.keepnotes.model.Note
import com.example.keepnotes.ui.base.BaseViewState

class NoteViewState(data: Data = Data(), error: Throwable? = null) :
    BaseViewState<NoteViewState.Data>(data, error) {
    data class Data(val isDeleted: Boolean = false, val note: Note? = null)
}