package com.todayrecord.todayrecord.screen.setting.alarm

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.viewModelScope
import com.todayrecord.todayrecord.data.repository.alarm.AlarmRepository
import com.todayrecord.todayrecord.model.alarm.Alarm
import com.todayrecord.todayrecord.screen.BaseViewModel
import com.todayrecord.todayrecord.util.type.EventFlow
import com.todayrecord.todayrecord.util.type.MutableEventFlow
import com.todayrecord.todayrecord.util.type.Result
import com.todayrecord.todayrecord.util.type.data
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.filterNotNull
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import java.time.ZonedDateTime
import java.time.format.DateTimeFormatter
import javax.inject.Inject

@HiltViewModel
class AlarmViewModel @Inject constructor(
    private val savedStateHandle: SavedStateHandle,
    private val alarmManager: TodayRecordAlarmManager,
    private val alarmRepository: AlarmRepository
) : BaseViewModel() {

    private val alarmState = alarmRepository.getAlarm()
        .stateIn(viewModelScope, SharingStarted.Lazily, Result.Loading)

    private val alarm = alarmState
        .map { it.data }
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), null)

    val alarmTime: StateFlow<String> = savedStateHandle
        .getStateFlow(KEY_ALARM_TIME, ZonedDateTime.now().format(DateTimeFormatter.ofPattern("HH:mm")))
    val alarmMessage: StateFlow<String?> = savedStateHandle.getStateFlow(KEY_ALARM_MESSAGE, null)
    val alarmEnabled: StateFlow<Boolean> = savedStateHandle.getStateFlow(KEY_ALARM_ENABLED, false)

    private val _enabledTodayRecordAlarmNotification = MutableStateFlow(false)
    val enabledTodayRecordAlarmNotification: StateFlow<Boolean> = _enabledTodayRecordAlarmNotification

    private val _navigateToTimePicker = MutableEventFlow<ZonedDateTime>()
    val navigateToTimePicker: EventFlow<ZonedDateTime> = _navigateToTimePicker

    private val _navigateToSystemSetting = MutableEventFlow<Unit>()
    val navigateToSystemSetting: EventFlow<Unit> = _navigateToSystemSetting

    private val _navigateToAlarmSaveAndBack = MutableEventFlow<Unit>()
    val navigateToAlarmSaveAndBack: EventFlow<Unit> = _navigateToAlarmSaveAndBack

    init {
        viewModelScope.launch {
            alarm.filterNotNull().collect {
                with(savedStateHandle) {
                    set(KEY_ALARM_TIME, it.alarmTime)
                    set(KEY_ALARM_MESSAGE, it.message)
                    set(KEY_ALARM_ENABLED, it.enabled)
                }
            }
        }
    }

    fun saveAlarm() {
        viewModelScope.launch {
            alarmRepository.setAlarm(
                Alarm(
                    alarmTime = alarmTime.value,
                    message = alarmMessage.value ?: "오늘 하루를 기록해보세요.",
                    enabled = alarmEnabled.value
                )
            ).collect {
                when (it) {
                    is Result.Loading -> return@collect
                    is Result.Success -> {
                        it.data?.let { alarm ->
                            if (alarm.enabled) {
                                alarmManager.startTodayRecordAlarm(alarm)
                            } else {
                                alarmManager.stopTodayRecordAlarm()
                            }
                        }
                        _navigateToAlarmSaveAndBack.emit(Unit)
                    }

                    is Result.Error -> setErrorMessage("알람 저장에 실패했습니다.")
                }
            }
        }
    }

    fun setAlarmTime(hour: Int, minute: Int) {
        val newAlarmTime = "${hour}:${minute}"
        if (alarmTime.value == newAlarmTime) return
        savedStateHandle.set(KEY_ALARM_TIME, newAlarmTime)
    }

    fun setAlarmMessage(message: String) {
        if (alarmMessage.value == message) return
        savedStateHandle.set(KEY_ALARM_MESSAGE, message)
    }

    fun setAlarmEnable(isEnabled: Boolean) {
        if (alarmEnabled.value == isEnabled) return
        savedStateHandle.set(KEY_ALARM_ENABLED, isEnabled)
    }

    fun setEnabledTodayRecordAlarmNotification(isEnabled: Boolean) {
        _enabledTodayRecordAlarmNotification.value = isEnabled
    }

    fun navigateToTimePicker() {
        viewModelScope.launch {
            val hourAndMinute = alarmTime.value.split(":")
            val alarmTime = ZonedDateTime.now().withHour(hourAndMinute[0].toInt()).withMinute(hourAndMinute[1].toInt())
            _navigateToTimePicker.emit(alarmTime)
        }
    }

    fun navigateToSystemSetting() {
        viewModelScope.launch {
            _navigateToSystemSetting.emit(Unit)
        }
    }

    companion object {
        private const val KEY_ALARM_TIME = "alarm_time"
        private const val KEY_ALARM_MESSAGE = "alarm_message"
        private const val KEY_ALARM_ENABLED = "alarm_enabled"
    }
}