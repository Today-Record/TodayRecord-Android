package com.todayrecord.presentation.receiver

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import com.todayrecord.domain.di.ApplicationScope
import com.todayrecord.presentation.screen.setting.alarm.TodayRecordAlarmManager
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch
import javax.inject.Inject

@AndroidEntryPoint
class InitializeReceiver: BroadcastReceiver() {

    @ApplicationScope
    @Inject
    lateinit var externalScope: CoroutineScope

    @Inject
    lateinit var alarmManager: TodayRecordAlarmManager

    override fun onReceive(context: Context, intent: Intent) {
        if (intent.action == Intent.ACTION_BOOT_COMPLETED || intent.action == Intent.ACTION_MY_PACKAGE_REPLACED) {
            externalScope.launch {
                initAlarm()
            }
        }
    }

    private fun initAlarm() {
        alarmManager.initializeTodayRecordAlarm()
    }
}