package com.todayrecord.todayrecord.screen.setting.binrecord

import androidx.lifecycle.viewModelScope
import androidx.paging.cachedIn
import com.todayrecord.todayrecord.data.repository.record.RecordRepository
import com.todayrecord.todayrecord.screen.BaseViewModel
import com.todayrecord.todayrecord.util.type.EventFlow
import com.todayrecord.todayrecord.util.type.MutableEventFlow
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class BinRecordsViewModel @Inject constructor(
    private val recordRepository: RecordRepository
) : BaseViewModel() {

    val binRecords = recordRepository.getRecords(true)
        .cachedIn(viewModelScope)

    private val _navigateToDetailRecord = MutableEventFlow<String>()
    val navigateToDetailRecord: EventFlow<String> = _navigateToDetailRecord

    private val _navigateToBinClearPopup = MutableEventFlow<Unit>()
    val navigateToBinClearPopup: EventFlow<Unit> = _navigateToBinClearPopup

    fun navigateToDetailRecord(recordId: String) {
        viewModelScope.launch {
            _navigateToDetailRecord.emit(recordId)
        }
    }

    fun navigateToBinClearPopup() {
        viewModelScope.launch {
            _navigateToBinClearPopup.emit(Unit)
        }
    }

    fun clearBinRecords() {
        viewModelScope.launch {
            recordRepository.clearBinRecords()
        }
    }
}