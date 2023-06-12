package com.todayrecord.presentation.screen.setting

import androidx.lifecycle.viewModelScope
import com.todayrecord.domain.usecase.record.ClearRecordsUseCase
import com.todayrecord.presentation.BuildConfig
import com.todayrecord.presentation.screen.BaseViewModel
import com.todayrecord.presentation.util.EventFlow
import com.todayrecord.presentation.util.MutableEventFlow
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SettingViewModel @Inject constructor(
    private val clearRecordsUseCase: ClearRecordsUseCase
) : BaseViewModel() {

    val appVersion: String = BuildConfig.VERSION_NAME

    private val _navigateToAlarmSetting = MutableEventFlow<Unit>()
    val navigateToAlarmSetting: EventFlow<Unit> = _navigateToAlarmSetting

    private val _navigateToBinRecords = MutableEventFlow<Unit>()
    val navigateToBinRecords: EventFlow<Unit> = _navigateToBinRecords

    private val _navigateToRecordsClearPopup = MutableEventFlow<Unit>()
    val navigateToRecordsClearPopup: EventFlow<Unit> = _navigateToRecordsClearPopup

    private val _navigateToRecordPrivacyPolicy = MutableEventFlow<Unit>()
    val navigateToRecordPrivacyPolicy: EventFlow<Unit> = _navigateToRecordPrivacyPolicy

    fun clearRecords() {
        viewModelScope.launch {
            clearRecordsUseCase(Unit)
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

    fun navigateToRecordPrivacyPolicy() {
        viewModelScope.launch {
            _navigateToRecordPrivacyPolicy.emit(Unit)
        }
    }
}