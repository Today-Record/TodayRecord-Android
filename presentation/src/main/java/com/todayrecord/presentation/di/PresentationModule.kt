package com.todayrecord.presentation.di

import android.app.AlarmManager
import android.app.NotificationManager
import android.content.Context
import com.google.android.play.core.appupdate.AppUpdateManager
import com.google.android.play.core.appupdate.AppUpdateManagerFactory
import com.todayrecord.domain.di.ApplicationScope
import com.todayrecord.domain.di.DefaultDispatcher
import com.todayrecord.domain.usecase.alarm.GetAlarmOneShotUseCase
import com.todayrecord.domain.usecase.alarm.GetAlarmUseCase
import com.todayrecord.presentation.screen.setting.alarm.TodayRecordAlarmManager
import com.todayrecord.presentation.util.manager.TodayRecordNotificationManager
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

    @ApplicationScope
    @Singleton
    @Provides
    fun providesApplicationScope(
        @DefaultDispatcher defaultDispatcher: CoroutineDispatcher
    ): CoroutineScope = CoroutineScope(SupervisorJob() + defaultDispatcher)

    @Singleton
    @Provides
    internal fun provideAppUpdateManager(
        @ApplicationContext context: Context
    ): AppUpdateManager = AppUpdateManagerFactory.create(context)

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

    @Singleton
    @Provides
    fun provideTodayRecordAlarmManager(
        @ApplicationContext context: Context,
        @ApplicationScope coroutineScope: CoroutineScope,
        alarmManager: AlarmManager,
        notificationManager: TodayRecordNotificationManager,
        getAlarmUseCase: GetAlarmUseCase,
        getAlarmOneShotUseCase: GetAlarmOneShotUseCase
    ): TodayRecordAlarmManager = TodayRecordAlarmManager(
        context,
        coroutineScope,
        alarmManager,
        notificationManager,
        getAlarmUseCase,
        getAlarmOneShotUseCase
    )
}