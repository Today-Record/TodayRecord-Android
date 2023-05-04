package com.todayrecord.todayrecord.data.repository.alarm

import com.todayrecord.todayrecord.data.datastore.PreferenceStorage
import com.todayrecord.todayrecord.model.alarm.Alarm
import com.todayrecord.todayrecord.util.type.Result
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class AlarmRepositoryImpl @Inject constructor(
    private val preferenceStorage: PreferenceStorage
) : AlarmRepository {

    override fun getAlarm(): Flow<Result<Alarm?>> {
        return preferenceStorage.alarm.map { Result.Success(it) }
    }

    override suspend fun getAlarmOneShot(): Result<Alarm?> {
        return preferenceStorage.alarm
            .map { Result.Success(it) }
            .first()
    }

    override fun setAlarm(alarm: Alarm): Flow<Result<Alarm?>> = flow {
        preferenceStorage.prefAlarm(alarm)
        emit(Result.Success(preferenceStorage.alarm.first()))
    }
}