package com.todayrecord.domain.usecase.alarm

import com.todayrecord.domain.di.IoDispatcher
import com.todayrecord.domain.usecase.UseCase
import com.todayrecord.domain.usecase.alarm.entity.AlarmEntity
import com.todayrecord.domain.util.PreferenceStorage
import kotlinx.coroutines.CoroutineDispatcher
import javax.inject.Inject

class SetAlarmUseCase @Inject constructor(
    @IoDispatcher private val ioDispatcher: CoroutineDispatcher,
    private val preferenceStorage: PreferenceStorage
): UseCase<SetAlarmUseCase.Params, Unit>(ioDispatcher) {

    override suspend fun execute(params: Params){
        return preferenceStorage.prefAlarm(alarm = params.alarm)
    }

    data class Params(
        val alarm: AlarmEntity
    )
}