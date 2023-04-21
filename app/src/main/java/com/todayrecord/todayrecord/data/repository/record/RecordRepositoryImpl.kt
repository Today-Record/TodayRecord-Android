package com.todayrecord.todayrecord.data.repository.record

import androidx.paging.PagingData
import com.todayrecord.todayrecord.model.record.Record
import com.todayrecord.todayrecord.util.Result
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

class RecordRepositoryImpl @Inject constructor(
    private val recordLocalRepository: RecordLocalRepository,
    private val recordRemoteRepository: RecordRemoteRepository
) : RecordRepository {

    override fun getRecords(): Flow<PagingData<Record>> {
        return recordLocalRepository.getRecords()
    }

    override suspend fun getRecord(recordId: String): Record? {
        return recordLocalRepository.getRecord(recordId)
    }

    override fun setRecord(record: Record) = flow {
        emit(Result.Loading)

        val uploadImages = record.images.filterNot { it[0] == 'h' }
        if (uploadImages.isNotEmpty()) {
            // TODO :: 이미지 업로드 작업
        } else {
            emit(Result.Success(recordLocalRepository.setRecord(record)))
        }
    }

    override suspend fun deleteRecord(recordId: String) {
        return recordLocalRepository.deleteRecord(recordId)
    }
}