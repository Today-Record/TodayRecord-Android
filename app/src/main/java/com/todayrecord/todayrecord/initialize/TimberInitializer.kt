package com.todayrecord.todayrecord.initialize

import android.content.Context
import androidx.startup.Initializer
import com.todayrecord.todayrecord.BuildConfig
import com.todayrecord.todayrecord.util.CrashlyticsMapper
import timber.log.Timber

@Suppress("unused")
class TimberInitializer : Initializer<Unit> {

    override fun create(context: Context) {
        if (BuildConfig.DEBUG) {
            Timber.plant(Timber.DebugTree())
        } else{
            Timber.plant(CrashlyticsMapper())
        }
    }

    override fun dependencies(): List<Class<out Initializer<*>>> {
        return emptyList()
    }
}