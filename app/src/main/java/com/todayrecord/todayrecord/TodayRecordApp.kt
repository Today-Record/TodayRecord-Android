package com.todayrecord.todayrecord

import android.app.Application
import android.content.Context
import com.google.firebase.FirebaseApp
import com.todayrecord.todayrecord.util.CrashlyticsMapper
import dagger.hilt.android.HiltAndroidApp
import timber.log.Timber

@HiltAndroidApp
open class TodayRecordApp : Application() {

    override fun onCreate() {
        super.onCreate()
        initFirebase(this)
        initTimber()
    }

    private fun initFirebase(context: Context) {
        FirebaseApp.initializeApp(context)
    }

    private fun initTimber() {
        if (BuildConfig.DEBUG) {
            Timber.plant(Timber.DebugTree())
        } else {
            Timber.plant(CrashlyticsMapper())
        }
    }
}