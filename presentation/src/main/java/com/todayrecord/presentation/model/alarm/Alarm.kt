package com.todayrecord.presentation.model.alarm

import com.todayrecord.domain.usecase.alarm.entity.AlarmEntity

data class Alarm(
    val alarmTime: String,
    val message: String,
    val enabled: Boolean
)

fun AlarmEntity.mapToItem(): Alarm {
    return Alarm(
        alarmTime = alarmTime,
        message = message,
        enabled = enabled
    )
}