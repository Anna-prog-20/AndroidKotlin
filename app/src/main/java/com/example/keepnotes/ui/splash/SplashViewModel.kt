package com.example.keepnotes.ui.splash

import com.example.keepnotes.model.NoAuthException
import com.example.keepnotes.model.Repository
import com.example.keepnotes.ui.base.BaseViewModel
import kotlinx.coroutines.launch

class SplashViewModel(private val repository: Repository) :
    BaseViewModel<Boolean>() {

    fun requestUser() {
        launch {
            repository.getCurrentUser()?.let {
                setData(true)
            } ?: setError(NoAuthException())
        }
    }
}