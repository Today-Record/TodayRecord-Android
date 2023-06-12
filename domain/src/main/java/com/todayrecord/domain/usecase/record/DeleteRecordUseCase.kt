package com.todayrecord.domain.usecase.record

import com.todayrecord.domain.di.IoDispatcher
import com.todayrecord.domain.usecase.UseCase
import kotlinx.coroutines.CoroutineDispatcher
import javax.inject.Inject

class DeleteRecordUseCase @Inject constructor(
    @IoDispatcher private val ioDispatcher: CoroutineDispatcher,
    private val recordRepository: RecordRepository
): UseCase<DeleteRecordUseCase.Params, Unit>(ioDispatcher) {

    override suspend fun execute(params: Params) {
        return recordRepository.deleteRecord(params.recordId)
    }

    data class Params(
        val recordId: String
    )
}