package com.todayrecord.todayrecord.screen

import androidx.lifecycle.viewModelScope
import com.todayrecord.todayrecord.data.repository.alarm.AlarmRepository
import com.todayrecord.todayrecord.model.alarm.Alarm
import com.todayrecord.todayrecord.screen.setting.alarm.TodayRecordAlarmManager
import com.todayrecord.todayrecord.util.type.data
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MainViewModel @Inject constructor(
    alarmRepository: AlarmRepository,
    private val alarmManager: TodayRecordAlarmManager
) : BaseViewModel() {

    private val alarmData: StateFlow<Alarm?> = alarmRepository.getAlarm()
        .map { it.data }
        .stateIn(viewModelScope, SharingStarted.Eagerly, null)

    fun initializeReceiver() {
        viewModelScope.launch {
            alarmData.collect {
                if (it != null) {
                    alarmManager.initializeTodayRecordAlarm()
                } else {
                    alarmManager.releaseTodayRecordAlarm()
                }
            }
        }
    }
}