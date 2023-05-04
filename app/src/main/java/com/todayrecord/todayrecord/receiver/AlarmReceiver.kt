package com.todayrecord.todayrecord.receiver

import android.app.AlarmManager
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import com.todayrecord.todayrecord.screen.setting.alarm.TodayRecordAlarmManager
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class AlarmReceiver : BroadcastReceiver() {

    @Inject
    lateinit var todayRecordAlarmManager: TodayRecordAlarmManager

    override fun onReceive(context: Context, intent: Intent) {
        when (intent.action) {
            AlarmManager.ACTION_SCHEDULE_EXACT_ALARM_PERMISSION_STATE_CHANGED -> {
                todayRecordAlarmManager.initializeTodayRecordAlarm()
            }

            else -> todayRecordAlarmManager.notifyTodayRecordAlarm()
        }
    }
}