package com.todayrecord.domain.usecase.appinfo

import com.todayrecord.domain.di.IoDispatcher
import com.todayrecord.domain.usecase.UseCase
import com.todayrecord.domain.util.PreferenceStorage
import kotlinx.coroutines.CoroutineDispatcher
import javax.inject.Inject

class SetInAppUpdateEnableUseCase  @Inject constructor(
    @IoDispatcher private val ioDispatcher: CoroutineDispatcher,
    private val preferenceStorage: PreferenceStorage
) : UseCase<Boolean, Unit>(ioDispatcher) {

    override suspend fun execute(params: Boolean) {
        return preferenceStorage.prefEnableInAppUpdate(params)
    }
}