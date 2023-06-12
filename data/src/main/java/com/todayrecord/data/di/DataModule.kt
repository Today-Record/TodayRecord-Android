package com.todayrecord.data.di

import com.todayrecord.data.source.repository.media.MediaRepositoryImpl
import com.todayrecord.data.source.repository.record.RecordLocalRepository
import com.todayrecord.data.source.repository.record.RecordRemoteRepository
import com.todayrecord.data.source.repository.record.RecordRepositoryImpl
import com.todayrecord.data.source.repository.record.local.RecordLocalRepositoryImpl
import com.todayrecord.data.source.repository.record.remote.RecordRemoteRepositoryImpl
import com.todayrecord.domain.usecase.media.MediaRepository
import com.todayrecord.domain.usecase.record.RecordRepository
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@InstallIn(SingletonComponent::class)
@Module
internal abstract class DataModule {

    @Singleton
    @Binds
    abstract fun bindRecordLocalRepository(recordLocalRepositoryImpl: RecordLocalRepositoryImpl): RecordLocalRepository

    @Singleton
    @Binds
    abstract fun bindRecordRemoteRepository(recordRemoteRepositoryImpl: RecordRemoteRepositoryImpl): RecordRemoteRepository

    @Singleton
    @Binds
    abstract fun bindRecordRepository(recordRepositoryImpl: RecordRepositoryImpl): RecordRepository

    @Singleton
    @Binds
    abstract fun bindMediaRepository(mediaRepositoryImpl: MediaRepositoryImpl): MediaRepository
}