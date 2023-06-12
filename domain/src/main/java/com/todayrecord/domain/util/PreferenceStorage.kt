package com.todayrecord.domain.util

import com.todayrecord.domain.usecase.alarm.entity.AlarmEntity
import kotlinx.coroutines.flow.Flow

interface PreferenceStorage {

    suspend fun prefAlarm(alarm: AlarmEntity)
    val alarm: Flow<AlarmEntity?>

    suspend fun prefEnableInAppUpdate(enable: Boolean)
    val enableInAppUpdate: Flow<Boolean>

    suspend fun prefInAppUpdateVersionCode(versionCode: Int)
    val inAppUpdateVersionCode: Flow<Int>
}