package com.todayrecord.domain.usecase.alarm.entity

import com.todayrecord.domain.usecase.Entity

data class AlarmEntity(
    val alarmTime: String,
    val message: String,
    val enabled: Boolean
): Entity