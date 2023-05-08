package com.todayrecord.todayrecord.screen.record

import android.net.Uri
import androidx.lifecycle.SavedStateHandle
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
class RecordsViewModel @Inject constructor(
    savedStateHandle: SavedStateHandle,
    recordRepository: RecordRepository
) : BaseViewModel() {

    private val notifyUrl = savedStateHandle.get<Uri?>(KEY_NOTIFY_URI)

    val records = recordRepository.getRecords(false)
        .cachedIn(viewModelScope)

    private val _navigateToWriteRecord = MutableEventFlow<Unit>()
    val navigateToWriteRecord: EventFlow<Unit> = _navigateToWriteRecord

    private val _navigateToDetailRecord = MutableEventFlow<String>()
    val navigateToDetailRecord: EventFlow<String> = _navigateToDetailRecord

    private val _navigateToSetting = MutableEventFlow<Unit>()
    val navigateToSetting: EventFlow<Unit> = _navigateToSetting

    init {
        if (notifyUrl != null) {
            navigateToDeepLink(notifyUrl)
            savedStateHandle.remove<Uri?>(KEY_NOTIFY_URI)
        }
    }

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

    companion object {
        private const val KEY_NOTIFY_URI = "notify_uri"
    }
}