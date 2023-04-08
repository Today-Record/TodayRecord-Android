package com.todayrecord.todayrecord.di

import android.content.Context
import com.todayrecord.todayrecord.data.database.RecordDatabase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@InstallIn(SingletonComponent::class)
@Module
object DataBaseModule {

    @Singleton
    @Provides
    fun providesRecordDatabase(
        @ApplicationContext context: Context
    ): RecordDatabase = RecordDatabase.buildDatabase(context)
}