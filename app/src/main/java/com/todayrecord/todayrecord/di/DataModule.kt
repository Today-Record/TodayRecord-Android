package com.todayrecord.todayrecord.di

import com.todayrecord.todayrecord.data.repository.alarm.AlarmRepository
import com.todayrecord.todayrecord.data.repository.alarm.AlarmRepositoryImpl
import com.todayrecord.todayrecord.data.repository.appinfo.AppInfoRepository
import com.todayrecord.todayrecord.data.repository.appinfo.AppInfoRepositoryImpl
import com.todayrecord.todayrecord.data.repository.media.MediaRepository
import com.todayrecord.todayrecord.data.repository.media.MediaRepositoryImpl
import com.todayrecord.todayrecord.data.repository.record.RecordLocalRepository
import com.todayrecord.todayrecord.data.repository.record.RecordRemoteRepository
import com.todayrecord.todayrecord.data.repository.record.RecordRepository
import com.todayrecord.todayrecord.data.repository.record.RecordRepositoryImpl
import com.todayrecord.todayrecord.data.repository.record.local.RecordLocalRepositoryImpl
import com.todayrecord.todayrecord.data.repository.record.remote.RecordRemoteRepositoryImpl
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@InstallIn(SingletonComponent::class)
@Module
abstract class DataModule {

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

    @Singleton
    @Binds
    abstract fun bindAlarmRepository(alarmRepositoryImpl: AlarmRepositoryImpl): AlarmRepository

    @Singleton
    @Binds
    abstract fun bindAppInfoRepository(appInfoRepositoryImpl: AppInfoRepositoryImpl): AppInfoRepository
}