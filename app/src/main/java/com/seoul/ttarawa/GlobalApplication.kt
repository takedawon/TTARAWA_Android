package com.seoul.ttarawa

import android.app.Application
import com.facebook.stetho.Stetho
import com.google.firebase.FirebaseApp
import timber.log.Timber

class GlobalApplication : Application() {

    override fun onCreate() {
        super.onCreate()
        FirebaseApp.initializeApp(this)
        Stetho.initializeWithDefaults(this)

        // init timber
        Timber.plant(Timber.DebugTree())
        // init stetho
        Stetho.initializeWithDefaults(this)
    }
}

