package com.todayrecord.todayrecord.data.repository.alarm

import com.todayrecord.todayrecord.model.alarm.Alarm
import com.todayrecord.todayrecord.util.type.Result
import kotlinx.coroutines.flow.Flow

interface AlarmRepository {

    fun getAlarm(): Flow<Result<Alarm?>>

    suspend fun getAlarmOneShot() : Result<Alarm?>

    fun setAlarm(alarm: Alarm): Flow<Result<Alarm?>>
}