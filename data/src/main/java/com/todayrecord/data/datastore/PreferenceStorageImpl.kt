package com.todayrecord.data.datastore

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.intPreferencesKey
import androidx.datastore.preferences.core.stringPreferencesKey
import com.squareup.moshi.Moshi
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
import com.todayrecord.data.datastore.PreferenceStorageImpl.PreferencesKeys.PREF_ALARM
import com.todayrecord.data.datastore.PreferenceStorageImpl.PreferencesKeys.PREF_ENABLED_IN_APP_UPDATE
import com.todayrecord.data.datastore.PreferenceStorageImpl.PreferencesKeys.PREF_IN_APP_UPDATE_VERSION_CODE
import com.todayrecord.data.source.repository.alarm.model.Alarm
import com.todayrecord.data.source.repository.alarm.model.mapToEntity
import com.todayrecord.data.source.repository.alarm.model.mapToModel
import com.todayrecord.domain.usecase.alarm.entity.AlarmEntity
import com.todayrecord.domain.util.PreferenceStorage
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import timber.log.Timber
import javax.inject.Inject

internal class PreferenceStorageImpl @Inject constructor(
    private val prefDatastore: DataStore<Preferences>
): PreferenceStorage {

    private val moshi = Moshi.Builder().add(KotlinJsonAdapterFactory()).build()
    private val alarmJsonAdapter = moshi.adapter(Alarm::class.java)

    companion object {
        const val PREFS_NAME = "PrefTodayRecord"
    }

    object PreferencesKeys {
        val PREF_ALARM = stringPreferencesKey("pref_alarm")

        val PREF_ENABLED_IN_APP_UPDATE = booleanPreferencesKey("pref_in_app_update_enabled")
        val PREF_IN_APP_UPDATE_VERSION_CODE = intPreferencesKey("pref_in_app_update_version_code")
    }

    override suspend fun prefAlarm(alarm: AlarmEntity) {
        prefDatastore.edit {
            it[PREF_ALARM] = try {
                alarmJsonAdapter.toJson(alarm.mapToModel())
            } catch (exception: Exception) {
                Timber.d("[PreferencesStorageImpl] save Alarm exception(${exception})")
                ""
            }
        }
    }

    override val alarm: Flow<AlarmEntity?> = prefDatastore.data
        .map {
            try {
                alarmJsonAdapter.fromJson(it[PREF_ALARM] ?: "")?.mapToEntity()
            } catch (exception: Exception) {
                Timber.d("[PreferencesStorageImpl] get alarm exception(${exception})")
                null
            }
        }

    override suspend fun prefEnableInAppUpdate(enable: Boolean) {
        prefDatastore.edit { it[PREF_ENABLED_IN_APP_UPDATE] = enable }
    }

    override val enableInAppUpdate: Flow<Boolean> = prefDatastore.data
        .map { it[PREF_ENABLED_IN_APP_UPDATE] ?: true }

    override suspend fun prefInAppUpdateVersionCode(versionCode: Int) {
        prefDatastore.edit { it[PREF_IN_APP_UPDATE_VERSION_CODE] = versionCode }
    }

    override val inAppUpdateVersionCode: Flow<Int> = prefDatastore.data
        .map { it[PREF_IN_APP_UPDATE_VERSION_CODE] ?: 0 }
}