package com.todayrecord.data.source.repository.record

interface RecordRemoteRepository {

    suspend fun imageUpload(images: List<String>): List<String>
}