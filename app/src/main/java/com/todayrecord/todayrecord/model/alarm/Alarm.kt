package com.todayrecord.todayrecord.model.alarm

data class Alarm(
    val alarmTime: String,
    val message: String,
    val enabled: Boolean
)