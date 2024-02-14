package com.todayrecord.todayrecord.initialize

import android.content.Context
import androidx.startup.Initializer
import com.google.firebase.FirebaseApp

@Suppress("unused")
class FirebaseInitializer : Initializer<Unit> {

    override fun create(context: Context) {
        FirebaseApp.initializeApp(context)
    }

    override fun dependencies(): List<Class<out Initializer<*>>> {
        return emptyList()
    }
}