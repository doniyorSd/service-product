package com.example.sero_service_admin

import android.app.Application
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase

class App : Application() {
    override fun onCreate() {
        Firebase.database.setPersistenceEnabled(true)
        super.onCreate()
    }
}