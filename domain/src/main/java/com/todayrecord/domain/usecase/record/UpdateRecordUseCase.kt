package com.todayrecord.domain.usecase.record

import com.todayrecord.domain.di.IoDispatcher
import com.todayrecord.domain.usecase.FlowUseCase
import com.todayrecord.domain.usecase.record.entity.RecordEntity
import com.todayrecord.domain.util.Result
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class UpdateRecordUseCase @Inject constructor(
    @IoDispatcher private val ioDispatcher: CoroutineDispatcher,
    private val recordRepository: RecordRepository
): FlowUseCase<UpdateRecordUseCase.Params, Unit>(ioDispatcher) {

    override fun execute(params: Params): Flow<Result<Unit>> {
        return recordRepository.updateRecord(params.record)
    }

    data class Params(
        val record: RecordEntity
    )
}