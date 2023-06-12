package com.todayrecord.presentation.screen.setting.alarm

import android.app.AlarmManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.os.Build
import androidx.core.app.AlarmManagerCompat
import com.todayrecord.domain.di.ApplicationScope
import com.todayrecord.domain.usecase.alarm.GetAlarmOneShotUseCase
import com.todayrecord.domain.usecase.alarm.GetAlarmUseCase
import com.todayrecord.domain.util.data
import com.todayrecord.presentation.R
import com.todayrecord.presentation.model.alarm.Alarm
import com.todayrecord.presentation.model.alarm.mapToItem
import com.todayrecord.presentation.receiver.AlarmReceiver
import com.todayrecord.presentation.util.Const.ALARM_NOTIFICATION_CHANNEL_ID
import com.todayrecord.presentation.util.manager.TodayRecordNotificationManager
import com.todayrecord.presentation.util.cancelIfActive
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import java.time.ZonedDateTime
import javax.inject.Inject

class TodayRecordAlarmManager @Inject constructor(
    @ApplicationContext private val context: Context,
    @ApplicationScope private val externalScope: CoroutineScope,
    private val alarmManager: AlarmManager,
    private val todayRecordNotificationManager: TodayRecordNotificationManager,
    private val getAlarmUseCase: GetAlarmUseCase,
    private val getAlarmOneShotUseCase: GetAlarmOneShotUseCase
) {

    private var todayRecordAlarmInitializeJob: Job? = null
    private var todayRecordAlarmReleaseJob: Job? = null

    fun initializeTodayRecordAlarm() {
        todayRecordAlarmInitializeJob.cancelIfActive()
        todayRecordAlarmInitializeJob = externalScope.launch {
            val alarm: Alarm? = getAlarmOneShotUseCase(Unit).data?.mapToItem()

            if (alarm != null) {
                todayRecordNotificationManager.cancel(ALARM_REQUEST_CODE_ID)

                if (alarm.enabled) {
                    startTodayRecordAlarm(alarm)
                } else {
                    stopTodayRecordAlarm()
                }
            }
        }
    }

    fun releaseTodayRecordAlarm() {
        todayRecordAlarmReleaseJob.cancelIfActive()
        todayRecordAlarmReleaseJob = externalScope.launch {
            todayRecordNotificationManager.cancel(ALARM_REQUEST_CODE_ID)
            stopTodayRecordAlarm()
        }
    }

    private fun syncTodayRecordAlarm(alarm: Alarm) {
        stopTodayRecordAlarm()
        startTodayRecordAlarm(alarm)
    }

    fun startTodayRecordAlarm(alarm: Alarm) {
        stopTodayRecordAlarm()
        val pendingIntentMode = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
            PendingIntent.FLAG_MUTABLE
        } else {
            PendingIntent.FLAG_UPDATE_CURRENT
        }
        val todayRecordAlarmIntent = Intent(context, AlarmReceiver::class.java)
        val todayRecordAlarmPendingIntent = PendingIntent.getBroadcast(
            context,
            ALARM_REQUEST_CODE_ID,
            todayRecordAlarmIntent,
            pendingIntentMode
        )
        var alarmTime = alarm.alarmTime.split(":")
            .let { time ->
                ZonedDateTime.now()
                    .withHour(time[0].toInt())
                    .withMinute(time[1].toInt())
                    .withSecond(0)
                    .withNano(0)
                    .toInstant()
                    .toEpochMilli()
            }

        if (System.currentTimeMillis() >= alarmTime) {
            alarmTime += AlarmManager.INTERVAL_DAY
        }

        AlarmManagerCompat.setExactAndAllowWhileIdle(
            alarmManager,
            AlarmManager.RTC_WAKEUP,
            alarmTime,
            todayRecordAlarmPendingIntent
        )
    }

    fun stopTodayRecordAlarm() {
        val pendingIntentMode = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
            PendingIntent.FLAG_MUTABLE
        } else {
            PendingIntent.FLAG_UPDATE_CURRENT
        }
        val todayRecordAlarmIntent = Intent(context, AlarmReceiver::class.java)
        val todayRecordAlarmPendingIntent = PendingIntent.getBroadcast(
            context,
            ALARM_REQUEST_CODE_ID,
            todayRecordAlarmIntent,
            pendingIntentMode
        )

        alarmManager.cancel(todayRecordAlarmPendingIntent)
        todayRecordAlarmPendingIntent.cancel()
    }

    fun notifyTodayRecordAlarm() {
        externalScope.launch {
            getAlarmUseCase(Unit).collect {
                it.data?.mapToItem()?.run {
                    createNotificationWithTodayRecordAlarm(this)
                    syncTodayRecordAlarm(this)
                }
            }
        }
    }

    private fun createNotificationWithTodayRecordAlarm(alarm: Alarm) {
        todayRecordNotificationManager
            .createNotificationBuilder(ALARM_NOTIFICATION_CHANNEL_ID)
            .setContentTitle(context.resources.getString(R.string.alarm_notify_title))
            .setContentText(alarm.message)
            .setContentIntent(todayRecordNotificationManager.getNotificationContentIntent(ALARM_REQUEST_CODE_ID))
            .let { todayRecordNotificationManager.notify(ALARM_REQUEST_CODE_ID, it) }
    }

    companion object {
        private const val ALARM_REQUEST_CODE_ID = 0
    }
}