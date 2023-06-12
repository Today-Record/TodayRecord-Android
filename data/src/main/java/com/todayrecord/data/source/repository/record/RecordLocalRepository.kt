package com.todayrecord.data.source.repository.record

import androidx.paging.PagingData
import com.todayrecord.domain.usecase.record.entity.RecordEntity
import kotlinx.coroutines.flow.Flow

interface RecordLocalRepository {

    fun getRecords(isDeleted: Boolean) : Flow<PagingData<RecordEntity>>

    suspend fun getRecord(recordId: String): RecordEntity?

    suspend fun createRecord(record: RecordEntity)

    suspend fun updateRecord(record: RecordEntity)

    suspend fun setRecordDelete(recordId: String, isDeleted: Boolean)

    suspend fun deleteRecord(recordId: String)

    suspend fun clearRecords()

    suspend fun clearBinRecords()
}