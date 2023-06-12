package com.todayrecord.domain.usecase.media

import androidx.paging.PagingData
import com.todayrecord.domain.usecase.media.entity.MediaEntity
import com.todayrecord.domain.util.Result
import kotlinx.coroutines.flow.Flow

interface MediaRepository {

    fun getMedias(): Flow<Result<PagingData<MediaEntity>>>
}