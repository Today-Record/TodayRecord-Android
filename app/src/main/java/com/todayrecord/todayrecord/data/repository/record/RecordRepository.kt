package com.todayrecord.todayrecord.data.repository.record

import androidx.paging.PagingData
import com.todayrecord.todayrecord.model.record.Record
import com.todayrecord.todayrecord.util.Result
import kotlinx.coroutines.flow.Flow

interface RecordRepository {

    fun getRecords() : Flow<PagingData<Record>>

    suspend fun getRecord(recordId: String): Record?

    fun setRecord(record: Record) : Flow<Result<Unit>>

    suspend fun deleteRecord(recordId: String)
}