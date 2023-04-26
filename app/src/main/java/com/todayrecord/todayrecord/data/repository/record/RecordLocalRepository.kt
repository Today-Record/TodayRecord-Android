package com.todayrecord.todayrecord.data.repository.record

import androidx.paging.PagingData
import com.todayrecord.todayrecord.model.record.Record
import kotlinx.coroutines.flow.Flow

interface RecordLocalRepository {

    fun getRecords() : Flow<PagingData<Record>>

    suspend fun getRecord(recordId: String): Record?

    suspend fun setRecord(record: Record)

    suspend fun setRecordDelete(recordId: String, isDeleted: Boolean)

    suspend fun deleteRecord(recordId: String)

    suspend fun clearRecords()
}