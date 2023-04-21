package com.todayrecord.todayrecord.data.repository.record

interface RecordRemoteRepository {

    suspend fun imageUpload(images: List<String>): List<String>
}