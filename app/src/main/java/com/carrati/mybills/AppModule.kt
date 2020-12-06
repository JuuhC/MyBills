package com.carrati.mybills

import android.app.Application
import org.koin.android.ext.koin.androidContext
import org.koin.android.ext.koin.androidLogger
import org.koin.core.context.startKoin

class AppModule : Application() {

    override fun onCreate() {
        super.onCreate()
        startKoin {
            androidLogger()
            androidContext(this@AppModule)
            modules(domainModule + dataModules + presentationModule)
        }
    }

    companion object {
        lateinit var instance: AppModule
    }

    init {
        instance = this
    }
}