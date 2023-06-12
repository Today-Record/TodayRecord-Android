package com.todayrecord.data.source.repository.record.local

import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import androidx.paging.map
import com.todayrecord.data.database.RecordDatabase
import com.todayrecord.data.source.repository.record.RecordLocalRepository
import com.todayrecord.data.source.repository.record.model.mapToEntity
import com.todayrecord.data.source.repository.record.model.mapToModel
import com.todayrecord.domain.usecase.record.entity.RecordEntity
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

internal class RecordLocalRepositoryImpl @Inject constructor(
    private val recordDatabase: RecordDatabase
) : RecordLocalRepository {

    override fun getRecords(isDeleted: Boolean): Flow<PagingData<RecordEntity>> = Pager(
        config = PagingConfig(pageSize = KEEP_LOAD_SIZE, enablePlaceholders = false),
        pagingSourceFactory = { recordDatabase.record().getRecords(isDeleted) }
    ).flow.map { pagingData -> pagingData.map { it.mapToEntity() } }

    override suspend fun getRecord(recordId: String): RecordEntity? {
        return recordDatabase.record().getRecord(recordId)?.mapToEntity()
    }

    override suspend fun setRecordDelete(recordId: String, isDeleted: Boolean) {
        recordDatabase.record().setRecordDeleted(recordId, isDeleted)
    }

    override suspend fun createRecord(record: RecordEntity) {
        recordDatabase.record().insertRecord(record.mapToModel())
    }

    override suspend fun updateRecord(record: RecordEntity) {
        recordDatabase.record().updateRecord(record.mapToModel())
    }

    override suspend fun deleteRecord(recordId: String) {
        recordDatabase.record().deleteRecord(recordId)
    }

    override suspend fun clearRecords() {
        recordDatabase.record().clearAllRecords()
    }

    override suspend fun clearBinRecords() {
        recordDatabase.record().clearBinRecords()
    }

    companion object {
        private const val KEEP_LOAD_SIZE = 20
    }
}