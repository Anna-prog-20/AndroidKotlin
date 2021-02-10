package com.example.keepnotes

import androidx.multidex.MultiDexApplication
import com.example.keepnotes.di.*
import org.koin.core.context.startKoin

class App : MultiDexApplication() {
    override fun onCreate() {
        super.onCreate()
        startKoin { modules(appModule, splashModule, mainModule, noteModule) }
    }
}