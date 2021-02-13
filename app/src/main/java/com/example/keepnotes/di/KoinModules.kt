package com.example.keepnotes.di

import com.example.keepnotes.model.FireStoreProvider
import com.example.keepnotes.model.RemoteDataProvider
import com.example.keepnotes.model.Repository
import com.example.keepnotes.ui.main.MainViewModel
import com.example.keepnotes.ui.note.NoteViewModel
import com.example.keepnotes.ui.splash.SplashViewModel
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import org.koin.android.viewmodel.dsl.viewModel
import org.koin.dsl.bind
import org.koin.dsl.module

val appModule = module {
    single { FirebaseAuth.getInstance() }
    single { FirebaseFirestore.getInstance() }
    single { FireStoreProvider(get(), get()) } bind RemoteDataProvider::class
    single { Repository(get()) }
}

val splashModule = module {
    viewModel { SplashViewModel(get()) }
}

val mainModule = module {
    viewModel { MainViewModel(get()) }
}

val noteModule = module {
    viewModel { NoteViewModel(get()) }
}