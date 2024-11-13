package com.example.tripplnannerapp.base.activities

import android.app.Application
import com.example.tripplnannerapp.base.dataBase.AppDatabase
import com.google.firebase.FirebaseApp
import com.pixplicity.easyprefs.library.Prefs

class App : Application() {
    override fun onCreate() {
        super.onCreate()
        FirebaseApp.initializeApp(this)
        AppDatabase.getDatabase(this)
        Prefs.Builder().setContext(this).build()

    }
}