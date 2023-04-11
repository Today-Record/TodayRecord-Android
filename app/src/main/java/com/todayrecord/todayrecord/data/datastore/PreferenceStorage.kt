package com.todayrecord.todayrecord.data.datastore

import com.todayrecord.todayrecord.model.alarm.Alarm
import kotlinx.coroutines.flow.Flow

interface PreferenceStorage {

    suspend fun prefAlarm(alarm: Alarm)
    val alarm: Flow<Alarm?>

}