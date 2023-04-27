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

    override fun getRecord(recordId: String): Flow<Result<Record?>> = flow {
        emit(Result.Loading)
        emit(Result.Success(recordLocalRepository.getRecord(recordId)))
    }

    override fun setRecord(record: Record): Flow<Result<Unit>> = flow {
        emit(Result.Loading)
        val uploadImages = record.images
            .mapIndexed { index, path -> index to path }
            .filterNot { it.second.first() == 'h' }

        if (uploadImages.isNotEmpty()) {
            val uploadImagesPaths = recordRemoteRepository.imageUpload(uploadImages.map { it.second })

            if (uploadImagesPaths.isNotEmpty()) {
                val newImagePaths = record.images
                    .toMutableList()
                    .apply {
                        uploadImages.mapIndexed { index, existingIndexData ->
                            this.set(existingIndexData.first, uploadImagesPaths[index])
                        }
                    }
                emit(Result.Success(recordLocalRepository.setRecord(record.copy(images = newImagePaths))))
            } else {
                emit(Result.Error(Exception(IMAGE_UPLOAD_FAIL)))
            }
        } else {
            emit(Result.Success(recordLocalRepository.setRecord(record)))
        }
    }

    override suspend fun setRecordDelete(recordId: String, isDeleted: Boolean) {
        recordLocalRepository.setRecordDelete(recordId, isDeleted)
    }

    override suspend fun deleteRecord(recordId: String) {
        return recordLocalRepository.deleteRecord(recordId)
    }

    override suspend fun clearRecords() {
        recordLocalRepository.clearRecords()
    }

    companion object {
        private const val IMAGE_UPLOAD_FAIL = "image upload fail exception!"
    }
}