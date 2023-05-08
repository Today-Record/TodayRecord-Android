package com.todayrecord.todayrecord.util.manager

import android.app.NotificationChannel
import android.app.NotificationChannelGroup
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Build
import androidx.annotation.RequiresApi
import androidx.core.app.NotificationCompat
import androidx.core.content.ContextCompat
import com.todayrecord.todayrecord.R
import com.todayrecord.todayrecord.util.Const.ALARM_NOTIFICATION_CHANNEL_ID
import com.todayrecord.todayrecord.util.Const.ALARM_NOTIFICATION_GROUP_ID
import dagger.hilt.android.qualifiers.ApplicationContext
import timber.log.Timber
import javax.inject.Inject

class TodayRecordNotificationManager @Inject constructor(
    @ApplicationContext private val context: Context,
    private val notificationManager: NotificationManager,
) {

    init {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            createChannelGroup()
            createChannel()
        }
    }

    @RequiresApi(Build.VERSION_CODES.O)
    private fun createChannelGroup() {
        Timber.d("[createChannelGroup]")
        val todayRecordChannelGroup = NotificationChannelGroup(ALARM_NOTIFICATION_GROUP_ID, NOTIFICATION_NAME)
        notificationManager.createNotificationChannelGroup(todayRecordChannelGroup)
    }

    @RequiresApi(Build.VERSION_CODES.O)
    private fun createChannel() {
        Timber.d("[createChannel]")
        val todayRecordAlarmChannel = NotificationChannel(ALARM_NOTIFICATION_CHANNEL_ID, NOTIFICATION_NAME, NotificationManager.IMPORTANCE_HIGH)
            .apply {
                group = ALARM_NOTIFICATION_GROUP_ID
                description = NOTIFICATION_NAME
                lockscreenVisibility = NotificationCompat.VISIBILITY_PUBLIC
                enableVibration(true)
                setShowBadge(true)
            }

        notificationManager.createNotificationChannel(todayRecordAlarmChannel)
    }

    fun createNotificationBuilder(channelId: String): NotificationCompat.Builder {
        Timber.d("[createNotificationBuilder]")
        return NotificationCompat.Builder(context, channelId)
            .setAutoCancel(true)
            .setPriority(NotificationCompat.PRIORITY_DEFAULT)
            .setColor(ContextCompat.getColor(context, R.color.color_474a54))
            .setSmallIcon(R.drawable.icon_quill)
            .setLargeIcon(null)
    }

    fun getNotificationContentIntent(requestCode: Int): PendingIntent {
        val contentIntent = Intent(Intent.ACTION_VIEW, Uri.parse("todayRecord://todayRecord/records/write"))
        return PendingIntent.getActivity(context, requestCode, contentIntent, if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) PendingIntent.FLAG_MUTABLE else PendingIntent.FLAG_UPDATE_CURRENT)
    }

    fun notify(id: Int, builder: NotificationCompat.Builder) = notificationManager.notify(id, builder.build())

    fun cancel(id: Int) = notificationManager.cancel(id)

    companion object {
        private const val NOTIFICATION_NAME = "today_record_alarm"
    }
}