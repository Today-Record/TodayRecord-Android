package com.todayrecord.todayrecord.data.repository.media

import androidx.paging.PagingData
import com.todayrecord.todayrecord.model.media.Media
import kotlinx.coroutines.flow.Flow

interface MediaRepository {

    fun getMedias(): Flow<PagingData<Media>>
}