package com.todayrecord.domain.usecase.media

import androidx.paging.PagingData
import com.todayrecord.domain.di.IoDispatcher
import com.todayrecord.domain.usecase.FlowUseCase
import com.todayrecord.domain.usecase.media.entity.MediaEntity
import com.todayrecord.domain.util.Result
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class GetMediaUseCase @Inject constructor(
    @IoDispatcher private val ioDispatcher: CoroutineDispatcher,
    private val mediaRepository: MediaRepository
): FlowUseCase<Unit, PagingData<MediaEntity>>(ioDispatcher) {

    override fun execute(params: Unit): Flow<Result<PagingData<MediaEntity>>> {
        return mediaRepository.getMedias()
    }
}