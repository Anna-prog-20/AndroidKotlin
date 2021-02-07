package com.example.keepnotes.ui

import android.app.Activity
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.keepnotes.model.Note
import com.example.keepnotes.model.Repository

class MainViewModel : ViewModel() {
    private val viewStateLiveData: MutableLiveData<MainViewState> = MutableLiveData()

    init {
        Repository.getNotes().observeForever {
            viewStateLiveData.value =
                viewStateLiveData.value?.copy(notes = it!!) ?: MainViewState(it!!)
        }
    }

    fun viewState(): LiveData<MainViewState> = viewStateLiveData

    fun setNotes(arrayList: List<Note>) {
        Repository.setNotes(arrayList.toMutableList())
    }
}