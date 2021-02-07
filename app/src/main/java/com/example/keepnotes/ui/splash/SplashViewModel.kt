package com.example.keepnotes.ui.splash

import com.example.keepnotes.model.NoAuthException
import com.example.keepnotes.model.Repository
import com.example.keepnotes.ui.base.BaseViewModel

class SplashViewModel(private val repository: Repository = Repository) :
    BaseViewModel<Boolean?, SplashViewState>() {

    fun requestUser() {
        repository.getCurrentUser().observeForever { user ->
            viewStateLiveData.value = user?.let {
                SplashViewState(isAuth = true)
            } ?: SplashViewState(error = NoAuthException())
        }
    }
}