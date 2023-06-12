package com.todayrecord.domain.usecase.alarm

import com.todayrecord.domain.di.IoDispatcher
import com.todayrecord.domain.usecase.UseCase
import com.todayrecord.domain.usecase.alarm.entity.AlarmEntity
import com.todayrecord.domain.util.PreferenceStorage
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.first
import javax.inject.Inject

class GetAlarmOneShotUseCase @Inject constructor(
    @IoDispatcher private val ioDispatcher: CoroutineDispatcher,
    private val preferenceStorage: PreferenceStorage
): UseCase<Unit, AlarmEntity?>(ioDispatcher) {

    override suspend fun execute(params: Unit): AlarmEntity? {
        return preferenceStorage.alarm.first()
    }
}