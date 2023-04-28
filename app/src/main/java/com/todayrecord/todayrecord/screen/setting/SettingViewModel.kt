package com.todayrecord.todayrecord.screen.setting

import androidx.lifecycle.viewModelScope
import com.todayrecord.todayrecord.BuildConfig
import com.todayrecord.todayrecord.data.repository.record.RecordRepository
import com.todayrecord.todayrecord.screen.BaseViewModel
import com.todayrecord.todayrecord.util.type.EventFlow
import com.todayrecord.todayrecord.util.type.MutableEventFlow
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SettingViewModel @Inject constructor(
    private val recordRepository: RecordRepository
) : BaseViewModel() {

    val appVersion: String = BuildConfig.VERSION_NAME

    private val _navigateToAlarmSetting = MutableEventFlow<Unit>()
    val navigateToAlarmSetting: EventFlow<Unit> = _navigateToAlarmSetting

    private val _navigateToBinRecords = MutableEventFlow<Unit>()
    val navigateToBinRecords: EventFlow<Unit> = _navigateToBinRecords

    private val _navigateToRecordsClearPopup = MutableEventFlow<Unit>()
    val navigateToRecordsClearPopup: EventFlow<Unit> = _navigateToRecordsClearPopup

    fun clearRecords() {
        viewModelScope.launch {
            recordRepository.clearRecords()
        }
    }

    fun navigateToAlarmSetting() {
        viewModelScope.launch {
            _navigateToAlarmSetting.emit(Unit)
        }
    }

    fun navigateToBinRecords() {
        viewModelScope.launch {
            _navigateToBinRecords.emit(Unit)
        }
    }

    fun navigateToRecordsClearPopup() {
        viewModelScope.launch {
            _navigateToRecordsClearPopup.emit(Unit)
        }
    }
}