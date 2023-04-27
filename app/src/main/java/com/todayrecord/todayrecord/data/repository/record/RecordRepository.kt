package com.todayrecord.todayrecord.data.repository.record

import androidx.paging.PagingData
import com.todayrecord.todayrecord.model.record.Record
import com.todayrecord.todayrecord.util.Result
import kotlinx.coroutines.flow.Flow

interface RecordRepository {

    fun getRecords(): Flow<PagingData<Record>>

    fun getRecord(recordId: String): Flow<Result<Record?>>

    fun setRecord(record: Record): Flow<Result<Unit>>

    suspend fun setRecordDelete(recordId: String, isDeleted: Boolean)

    suspend fun deleteRecord(recordId: String)

    suspend fun clearRecords()
}