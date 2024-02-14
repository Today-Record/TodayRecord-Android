package com.todayrecord.presentation.screen

import androidx.lifecycle.viewModelScope
import com.todayrecord.domain.usecase.alarm.GetAlarmUseCase
import com.todayrecord.domain.usecase.appinfo.GetInAppUpdateCodeUseCase
import com.todayrecord.domain.usecase.appinfo.GetInAppUpdateEnableUseCase
import com.todayrecord.domain.usecase.appinfo.SetInAppUpdateCodeUseCase
import com.todayrecord.domain.usecase.appinfo.SetInAppUpdateEnableUseCase
import com.todayrecord.domain.util.data
import com.todayrecord.presentation.model.alarm.Alarm
import com.todayrecord.presentation.model.alarm.mapToItem
import com.todayrecord.presentation.screen.base.BaseViewModel
import com.todayrecord.presentation.screen.setting.alarm.TodayRecordAlarmManager
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
    private val alarmManager: TodayRecordAlarmManager,
    private val setInAppUpdateEnableUseCase: SetInAppUpdateEnableUseCase,
    private val setInAppUpdateCodeUseCase: SetInAppUpdateCodeUseCase,
    getAlarmUseCase: GetAlarmUseCase,
    getInAppUpdateEnableUseCase: GetInAppUpdateEnableUseCase,
    getInAppUpdateCodeUseCase: GetInAppUpdateCodeUseCase,
) : BaseViewModel() {

    val enableInAppUpdate: StateFlow<Boolean> = getInAppUpdateEnableUseCase(Unit)
        .mapLatest { it.data ?: false }
        .stateIn(viewModelScope, SharingStarted.Eagerly, false)

    val inAppUpdateVersionCode: StateFlow<Int> = getInAppUpdateCodeUseCase(Unit)
        .mapLatest { it.data ?: 0 }
        .stateIn(viewModelScope, SharingStarted.Eagerly, 0)

    private val alarmData: StateFlow<Alarm?> = getAlarmUseCase(Unit)
        .map { it.data?.mapToItem() }
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
            setInAppUpdateEnableUseCase(isEnable)
        }
    }

    fun setInAppUpdateVersionCode(versionCode: Int) {
        viewModelScope.launch {
            setInAppUpdateCodeUseCase(versionCode)
        }
    }
}