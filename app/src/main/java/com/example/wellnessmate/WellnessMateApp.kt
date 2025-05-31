package com.example.wellnessmate

import android.app.Application
import com.google.firebase.FirebaseApp

class WellnessMateApp : Application() {
    override fun onCreate() {
        super.onCreate()
        FirebaseApp.initializeApp(this) // ✅ This initializes Firebase SDK
    }
}