package com.todayrecord.todayrecord.data.datastore

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import com.squareup.moshi.Moshi
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
import com.todayrecord.todayrecord.data.datastore.PreferenceStorageImpl.PreferencesKeys.PREF_ALARM
import com.todayrecord.todayrecord.model.alarm.Alarm
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import timber.log.Timber
import javax.inject.Inject

class PreferenceStorageImpl @Inject constructor(
    private val prefDatastore: DataStore<Preferences>
) : PreferenceStorage {

    private val moshi = Moshi.Builder().add(KotlinJsonAdapterFactory()).build()
    private val alarmJsonAdapter = moshi.adapter(Alarm::class.java)

    companion object {
        const val PREFS_NAME = "PrefTodayRecord"
    }

    object PreferencesKeys {
        val PREF_ALARM = stringPreferencesKey("pref_alarm")
    }

    override suspend fun prefAlarm(alarm: Alarm) {
        prefDatastore.edit {
            it[PREF_ALARM] = try {
                alarmJsonAdapter.toJson(alarm)
            } catch (exception: Exception) {
                Timber.d("[PreferencesStorageImpl] save Alarm exception(${exception})")
                ""
            }
        }
    }

    override val alarm: Flow<Alarm?> = prefDatastore.data.map {
        try {
            alarmJsonAdapter.fromJson(it[PREF_ALARM] ?: "")
        } catch (exception: Exception) {
            Timber.d("[PreferencesStorageImpl] get alarm exception(${exception})")
            null
        }
    }
}