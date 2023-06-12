package com.todayrecord.data.source.repository.record

import androidx.paging.PagingData
import com.todayrecord.domain.usecase.record.RecordRepository
import com.todayrecord.domain.usecase.record.entity.RecordEntity
import com.todayrecord.domain.util.Result
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

internal class RecordRepositoryImpl @Inject constructor(
    private val recordLocalRepository: RecordLocalRepository,
    private val recordRemoteRepository: RecordRemoteRepository
): RecordRepository {

    override fun getRecords(isDeleted: Boolean): Flow<Result<PagingData<RecordEntity>>> {
        return recordLocalRepository.getRecords(isDeleted).map { Result.Success(it) }
    }

    override fun getRecord(recordId: String): Flow<Result<RecordEntity?>> = flow {
        emit(Result.Loading)
        emit(Result.Success(recordLocalRepository.getRecord(recordId)))
    }

    override fun createRecord(record: RecordEntity): Flow<Result<Unit>> = flow {
        emit(Result.Loading)

        val uploadImages = record.images
            .mapIndexed { index, path -> index to path }
            .filterNot { it.second.first() == 'h' }

        if (uploadImages.isNotEmpty()) {
            val newImagesPaths = getNewRecordImagesPath(
                record.images,
                uploadImages.map { it.first },
                recordRemoteRepository.imageUpload(uploadImages.map { it.second })
            )
            emit(Result.Success(recordLocalRepository.createRecord(record.copy(images = newImagesPaths))))
        } else {
            emit(Result.Success(recordLocalRepository.createRecord(record)))
        }
    }

    override fun updateRecord(record: RecordEntity): Flow<Result<Unit>> = flow {
        emit(Result.Loading)
        val uploadImages = record.images
            .mapIndexed { index, path -> index to path }
            .filterNot { it.second.first() == 'h' }

        if (uploadImages.isNotEmpty()) {
            val newImagesPaths = getNewRecordImagesPath(
                record.images,
                uploadImages.map { it.first },
                recordRemoteRepository.imageUpload(uploadImages.map { it.second })
            )

            emit(Result.Success(recordLocalRepository.updateRecord(record.copy(images = newImagesPaths))))
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

    override suspend fun clearBinRecords() {
        recordLocalRepository.clearBinRecords()
    }
}