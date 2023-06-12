package com.todayrecord.domain.usecase.record

import androidx.paging.PagingData
import com.todayrecord.domain.usecase.record.entity.RecordEntity
import com.todayrecord.domain.util.Result
import kotlinx.coroutines.flow.Flow

interface RecordRepository {

    fun getRecords(isDeleted: Boolean): Flow<Result<PagingData<RecordEntity>>>

    fun getRecord(recordId: String): Flow<Result<RecordEntity?>>

    fun createRecord(record: RecordEntity): Flow<Result<Unit>>

    fun updateRecord(record: RecordEntity): Flow<Result<Unit>>

    suspend fun setRecordDelete(recordId: String, isDeleted: Boolean)

    suspend fun deleteRecord(recordId: String)

    suspend fun clearRecords()

    suspend fun clearBinRecords()
}