package com.todayrecord.todayrecord.data.repository.record

import androidx.paging.PagingData
import com.todayrecord.todayrecord.model.record.Record
import com.todayrecord.todayrecord.util.Result
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import java.lang.Exception
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

    override fun setRecord(record: Record): Flow<Result<Unit>> = flow {
        emit(Result.Loading)

        val uploadImages = record.images.mapIndexed { index, path ->
            if (path[0] == 'h') null else index to path
        }.filterNotNull()

        if (uploadImages.isNotEmpty()) {
            val uploadImagesPaths = recordRemoteRepository.imageUpload(uploadImages.map { it.second })

            if (uploadImagesPaths.isNotEmpty()) {
                val newImagePath = record.images
                    .toMutableList()
                    .apply {
                        uploadImages.map {
                            set(it.first, uploadImagesPaths[it.first])
                        }
                    }

                emit(Result.Success(recordLocalRepository.setRecord(record.copy(images = newImagePath))))
            } else {
                emit(Result.Error(Exception(IMAGE_UPLOAD_FAIL)))
            }
        } else {
            emit(Result.Success(recordLocalRepository.setRecord(record)))
        }
    }

    override suspend fun deleteRecord(recordId: String) {
        return recordLocalRepository.deleteRecord(recordId)
    }

    companion object {
        private const val IMAGE_UPLOAD_FAIL  = "image upload fail exception!"
    }
}