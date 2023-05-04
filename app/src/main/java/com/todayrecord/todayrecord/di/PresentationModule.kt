package com.todayrecord.todayrecord.di

import android.app.AlarmManager
import android.app.NotificationManager
import android.content.Context
import com.todayrecord.todayrecord.data.repository.alarm.AlarmRepository
import com.todayrecord.todayrecord.screen.setting.alarm.TodayRecordAlarmManager
import com.todayrecord.todayrecord.util.manager.TodayRecordNotificationManager
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.SupervisorJob
import javax.inject.Singleton

@InstallIn(SingletonComponent::class)
@Module
object PresentationModule {

    @Singleton
    @Provides
    internal fun provideAlarmManager(
        @ApplicationContext context: Context
    ): AlarmManager = context.getSystemService(Context.ALARM_SERVICE) as AlarmManager

    @Singleton
    @Provides
    internal fun provideNotificationManager(
        @ApplicationContext context: Context
    ): NotificationManager = context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager

    @ApplicationScope
    @Singleton
    @Provides
    fun providesApplicationScope(
        @DefaultDispatcher defaultDispatcher: CoroutineDispatcher
    ): CoroutineScope = CoroutineScope(SupervisorJob() + defaultDispatcher)

    @Singleton
    @Provides
    fun provideTodayRecordAlarmManager(
        @ApplicationContext context: Context,
        @ApplicationScope coroutineScope: CoroutineScope,
        alarmManager: AlarmManager,
        notificationManager: TodayRecordNotificationManager,
        alarmRepository: AlarmRepository
    ): TodayRecordAlarmManager = TodayRecordAlarmManager(
        context,
        coroutineScope,
        alarmManager,
        notificationManager,
        alarmRepository
    )
}