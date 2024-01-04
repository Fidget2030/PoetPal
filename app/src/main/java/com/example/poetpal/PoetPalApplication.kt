package com.example.poetpal

import android.app.Application
import com.example.poetpal.data.AppContainer
import com.example.poetpal.data.DefaultAppContainer

class PoetPalApplication : Application() {
    lateinit var container: AppContainer

    override fun onCreate() {
        super.onCreate()
        container = DefaultAppContainer(context = applicationContext)
    }
}
