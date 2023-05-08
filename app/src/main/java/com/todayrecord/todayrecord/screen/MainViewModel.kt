package com.todayrecord.todayrecord.screen

import androidx.lifecycle.viewModelScope
import com.todayrecord.todayrecord.data.repository.alarm.AlarmRepository
import com.todayrecord.todayrecord.data.repository.appinfo.AppInfoRepository
import com.todayrecord.todayrecord.model.alarm.Alarm
import com.todayrecord.todayrecord.screen.setting.alarm.TodayRecordAlarmManager
import com.todayrecord.todayrecord.util.type.data
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.mapLatest
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MainViewModel @Inject constructor(
    alarmRepository: AlarmRepository,
    private val alarmManager: TodayRecordAlarmManager,
    private val appInfoRepository: AppInfoRepository
) : BaseViewModel() {

    val enableInAppUpdate: StateFlow<Boolean> = appInfoRepository.getInAppUpdateEnable()
        .mapLatest { it.data ?: false }
        .stateIn(viewModelScope, SharingStarted.Eagerly, false)

    val inAppUpdateVersionCode: StateFlow<Int> = appInfoRepository.getInAppUpdateCode()
        .mapLatest { it.data ?: 0 }
        .stateIn(viewModelScope, SharingStarted.Eagerly, 0)

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

    fun setEnableInAppUpdate(isEnable: Boolean) {
        viewModelScope.launch {
            appInfoRepository.setInAppUpdateEnable(isEnable)
        }
    }

    fun setInAppUpdateVersionCode(versionCode: Int) {
        viewModelScope.launch {
            appInfoRepository.setInAppUpdateCode(versionCode)
        }
    }
}