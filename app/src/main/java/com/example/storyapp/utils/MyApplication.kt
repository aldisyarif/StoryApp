package com.example.storyapp.utils

import android.app.Application
import android.content.Context
import dagger.hilt.android.HiltAndroidApp

@HiltAndroidApp
class MyApplication: Application() {
    companion object {
        var applicationContext: Context? = null
    }

    override fun onCreate() {
        super.onCreate()
        Companion.applicationContext = this.applicationContext
    }
}