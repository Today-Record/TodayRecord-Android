package com.todayrecord.data.source.repository.alarm.model

import com.todayrecord.data.source.Model
import com.todayrecord.domain.usecase.alarm.entity.AlarmEntity

internal data class Alarm(
    val alarmTime: String,
    val message: String,
    val enabled: Boolean
) : Model

internal fun Alarm.mapToEntity(): AlarmEntity {
    return AlarmEntity(
        alarmTime = alarmTime,
        message = message,
        enabled = enabled
    )
}

internal fun AlarmEntity.mapToModel(): Alarm {
    return Alarm(
        alarmTime = alarmTime,
        message = message,
        enabled = enabled
    )
}