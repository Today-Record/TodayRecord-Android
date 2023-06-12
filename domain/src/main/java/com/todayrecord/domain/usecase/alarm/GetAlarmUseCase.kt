package com.todayrecord.domain.usecase.alarm

import com.todayrecord.domain.di.IoDispatcher
import com.todayrecord.domain.usecase.FlowUseCase
import com.todayrecord.domain.usecase.alarm.entity.AlarmEntity
import com.todayrecord.domain.util.PreferenceStorage
import com.todayrecord.domain.util.Result
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class GetAlarmUseCase @Inject constructor(
    @IoDispatcher private val ioDispatcher: CoroutineDispatcher,
    private val preferenceStorage: PreferenceStorage
) : FlowUseCase<Unit, AlarmEntity?>(ioDispatcher) {

    override fun execute(params: Unit): Flow<Result<AlarmEntity?>> {
        return preferenceStorage.alarm.map {
            Result.Success(it)
        }
    }
}