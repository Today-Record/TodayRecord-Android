package com.todayrecord.domain.usecase.record

import androidx.paging.PagingData
import com.todayrecord.domain.di.IoDispatcher
import com.todayrecord.domain.usecase.FlowUseCase
import com.todayrecord.domain.usecase.record.entity.RecordEntity
import com.todayrecord.domain.util.Result
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class GetRecordsUseCase @Inject constructor(
    @IoDispatcher private val ioDispatcher: CoroutineDispatcher,
    private val recordRepository: RecordRepository
): FlowUseCase<Boolean, PagingData<RecordEntity>>(ioDispatcher) {

    override fun execute(params: Boolean): Flow<Result<PagingData<RecordEntity>>> {
        return recordRepository.getRecords(params)
    }
}