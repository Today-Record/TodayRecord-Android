package com.todayrecord.presentation.di

import android.app.NotificationManager
import android.content.Context
import com.todayrecord.presentation.util.manager.TodayRecordNotificationManager
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@InstallIn(SingletonComponent::class)
@Module
object UtilModule {

    @Singleton
    @Provides
    fun provideTodayRecordNotificationManager(
        @ApplicationContext context: Context,
        notificationManager: NotificationManager
    ): TodayRecordNotificationManager = TodayRecordNotificationManager(context, notificationManager)
}