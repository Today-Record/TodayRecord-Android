package com.todayrecord.todayrecord.data.repository.record.remote

import com.todayrecord.todayrecord.data.repository.record.RecordRemoteRepository
import com.todayrecord.todayrecord.util.FileUtil
import javax.inject.Inject

class RecordRemoteRepositoryImpl @Inject constructor(
    private val fileUtil: FileUtil
) : RecordRemoteRepository {

    override suspend fun imageUpload(images: List<String>): List<String> {
        // TODO("이미지 주소를 파일로 변경하여 스토리지에 업로드")
        return emptyList()
    }
}