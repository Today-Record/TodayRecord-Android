package com.todayrecord.todayrecord.data.repository.record.local

import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import com.todayrecord.todayrecord.data.database.RecordDatabase
import com.todayrecord.todayrecord.data.repository.record.RecordLocalRepository
import com.todayrecord.todayrecord.model.record.Record
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class RecordLocalRepositoryImpl @Inject constructor(
    private val recordDatabase: RecordDatabase
): RecordLocalRepository {

    override fun getRecords(): Flow<PagingData<Record>> = Pager(
        config = PagingConfig(pageSize = KEEP_LOAD_SIZE, enablePlaceholders = false),
        pagingSourceFactory = { recordDatabase.record().getRecords() }
    ).flow

    override suspend fun getRecord(recordId: String): Record? {
        return recordDatabase.record().getRecord(recordId)
    }

    override suspend fun setRecord(record: Record) {
        recordDatabase.record().insertRecord(record)
    }

    override suspend fun deleteRecord(recordId: String) {
        recordDatabase.record().deleteRecord(recordId)
    }

    companion object {
        private const val KEEP_LOAD_SIZE = 20
    }
}