package com.todayrecord.todayrecord.data.repository.record.remote

import com.todayrecord.todayrecord.data.repository.record.RecordRemoteRepository
import com.todayrecord.todayrecord.util.file.CompressorUtil
import com.todayrecord.todayrecord.util.file.FileUtil
import com.todayrecord.todayrecord.util.file.FirebaseStorageUtil
import kotlinx.coroutines.withTimeout
import javax.inject.Inject

class RecordRemoteRepositoryImpl @Inject constructor(
    private val fileUtil: FileUtil,
    private val compressorUtil: CompressorUtil,
    private val firebaseStorageUtil: FirebaseStorageUtil
) : RecordRemoteRepository {

    override suspend fun imageUpload(images: List<String>): List<String> {
        return try {
            withTimeout(15_000L) {
                images.map {
                    fileUtil.from(it).let { uploadFile ->
                        compressorUtil.compressFile(uploadFile).let { compressorFile ->
                            firebaseStorageUtil.uploadImageToFirebase(compressorFile)
                        }
                    }
                }
            }
        } catch (exception: Exception) {
            throw Exception("image upload fail exception!")
        }
    }
}