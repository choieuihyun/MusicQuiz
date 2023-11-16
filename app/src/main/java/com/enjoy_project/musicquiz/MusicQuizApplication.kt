package com.enjoy_project.musicquiz

import android.app.Application
import com.google.firebase.FirebaseApp
import com.google.firebase.storage.FirebaseStorage

class MusicQuizApplication : Application() {

    override fun onCreate() {
        super.onCreate()

        FirebaseApp.initializeApp(this)

        val storage = FirebaseStorage.getInstance()
        val storageRef = storage.reference
        val songRef = storageRef.child("test")

    }
}