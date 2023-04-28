package com.todayrecord.todayrecord.screen.record

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.viewModelScope
import com.todayrecord.todayrecord.screen.BaseViewModel
import com.todayrecord.todayrecord.util.type.EventFlow
import com.todayrecord.todayrecord.util.type.MutableEventFlow
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class RecordsViewModel @Inject constructor(
    savedStateHandle: SavedStateHandle
) : BaseViewModel() {

    private val _navigateToWriteRecord = MutableEventFlow<Unit>()
    val navigateToWriteRecord: EventFlow<Unit> = _navigateToWriteRecord

    private val _navigateToDetailRecord = MutableEventFlow<String>()
    val navigateToDetailRecord: EventFlow<String> = _navigateToDetailRecord

    private val _navigateToSetting = MutableEventFlow<Unit>()
    val navigateToSetting: EventFlow<Unit> = _navigateToSetting

    fun navigateToWriteRecord() {
        viewModelScope.launch {
            _navigateToWriteRecord.emit(Unit)
        }
    }

    fun navigateToDetailRecord(recordId: String) {
        viewModelScope.launch {
            _navigateToDetailRecord.emit(recordId)
        }
    }

    fun navigateToSetting() {
        viewModelScope.launch {
            _navigateToSetting.emit(Unit)
        }
    }
}