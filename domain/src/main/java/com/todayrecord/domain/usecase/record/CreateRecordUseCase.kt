package com.todayrecord.domain.usecase.record

import com.todayrecord.domain.di.IoDispatcher
import com.todayrecord.domain.usecase.FlowUseCase
import com.todayrecord.domain.usecase.record.entity.RecordEntity
import com.todayrecord.domain.util.Result
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class CreateRecordUseCase @Inject constructor(
    @IoDispatcher private val ioDispatcher: CoroutineDispatcher,
    private val recordRepository: RecordRepository
) : FlowUseCase<CreateRecordUseCase.Params, Unit>(ioDispatcher) {

    override fun execute(params: Params): Flow<Result<Unit>> {
        return recordRepository.createRecord(params.record)
    }

    data class Params(
        val record: RecordEntity
    )
}