package com.todayrecord.domain.usecase.record

import com.todayrecord.domain.di.IoDispatcher
import com.todayrecord.domain.usecase.FlowUseCase
import com.todayrecord.domain.usecase.record.entity.RecordEntity
import com.todayrecord.domain.util.Result
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class GetRecordUseCase @Inject constructor(
    @IoDispatcher private val ioDispatcher: CoroutineDispatcher,
    private val recordRepository: RecordRepository
): FlowUseCase<GetRecordUseCase.Params, RecordEntity?>(ioDispatcher) {

    override fun execute(params: Params): Flow<Result<RecordEntity?>> {
        return recordRepository.getRecord(params.recordId)
    }

    data class Params(
        val recordId: String
    )
}