package com.todayrecord.domain.usecase.appinfo

import com.todayrecord.domain.di.IoDispatcher
import com.todayrecord.domain.usecase.FlowUseCase
import com.todayrecord.domain.util.PreferenceStorage
import com.todayrecord.domain.util.Result
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class GetInAppUpdateEnableUseCase @Inject constructor(
    @IoDispatcher private val ioDispatcher: CoroutineDispatcher,
    private val preferenceStorage: PreferenceStorage
): FlowUseCase<Unit, Boolean>(ioDispatcher) {

    override fun execute(params: Unit): Flow<Result<Boolean>> {
        return preferenceStorage.enableInAppUpdate.map {
            Result.Success(it)
        }
    }
}