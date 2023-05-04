package com.todayrecord.todayrecord.data.repository.record

import androidx.paging.PagingData
import com.todayrecord.todayrecord.model.record.Record
import com.todayrecord.todayrecord.util.type.Result
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

    override fun createRecord(record: Record): Flow<Result<Unit>> = flow {
        emit(Result.Loading)

        val uploadImages = record.images
            .mapIndexed { index, path -> index to path }
            .filterNot { it.second.first() == 'h' }

        if (uploadImages.isNotEmpty()) {
            val uploadImagesPaths = recordRemoteRepository.imageUpload(uploadImages.map { it.second })

            getNewRecordImagesPath(record.images, uploadImages.map { it.first }, uploadImagesPaths).let {
                if (it.isNotEmpty()) {
                    emit(Result.Success(recordLocalRepository.createRecord(record.copy(images = it))))
                } else {
                    emit(Result.Error(Exception(IMAGE_UPLOAD_FAIL)))
                }
            }
        } else {
            emit(Result.Success(recordLocalRepository.createRecord(record)))
        }
    }

    override fun updateRecord(record: Record): Flow<Result<Unit>> = flow {
        emit(Result.Loading)
        val uploadImages = record.images
            .mapIndexed { index, path -> index to path }
            .filterNot { it.second.first() == 'h' }

        if (uploadImages.isNotEmpty()) {
            val uploadImagesPaths = recordRemoteRepository.imageUpload(uploadImages.map { it.second })

            getNewRecordImagesPath(record.images, uploadImages.map { it.first }, uploadImagesPaths).let {
                if (it.isNotEmpty()) {
                    emit(Result.Success(recordLocalRepository.updateRecord(record.copy(images = it))))
                } else {
                    emit(Result.Error(Exception(IMAGE_UPLOAD_FAIL)))
                }
            }
        } else {
            emit(Result.Success(recordLocalRepository.updateRecord(record)))
        }
    }

    private fun getNewRecordImagesPath(recordImages: List<String>, existingIndex: List<Int>, uploadImagesPaths: List<String>): List<String> {
        return if (uploadImagesPaths.isNotEmpty()) {
            recordImages.toMutableList().apply {
                existingIndex.mapIndexed { index, existingIndex ->
                    set(existingIndex, uploadImagesPaths[index])
                }
            }
        } else {
            emptyList()
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