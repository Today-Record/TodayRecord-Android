package com.todayrecord.todayrecord.data.datastore

import com.todayrecord.todayrecord.model.alarm.Alarm
import kotlinx.coroutines.flow.Flow

interface PreferenceStorage {

    suspend fun prefAlarm(alarm: Alarm)
    val alarm: Flow<Alarm?>

    suspend fun prefEnableInAppUpdate(enable: Boolean)
    val enableInAppUpdate: Flow<Boolean>

    suspend fun prefInAppUpdateVersionCode(versionCode: Int)
    val inAppUpdateVersionCode: Flow<Int>
}