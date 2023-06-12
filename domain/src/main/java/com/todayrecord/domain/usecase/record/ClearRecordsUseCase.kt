package com.todayrecord.domain.usecase.record

import com.todayrecord.domain.di.IoDispatcher
import com.todayrecord.domain.usecase.UseCase
import kotlinx.coroutines.CoroutineDispatcher
import javax.inject.Inject

class ClearRecordsUseCase @Inject constructor(
    @IoDispatcher private val ioDispatcher: CoroutineDispatcher,
    private val recordRepository: RecordRepository
) : UseCase<Unit, Unit>(ioDispatcher) {

    override suspend fun execute(params: Unit) {
        return recordRepository.clearRecords()
    }
}