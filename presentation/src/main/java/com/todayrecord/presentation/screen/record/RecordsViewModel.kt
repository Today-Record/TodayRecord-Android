package com.todayrecord.presentation.screen.record

import android.net.Uri
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.viewModelScope
import androidx.paging.cachedIn
import androidx.paging.map
import com.todayrecord.domain.usecase.record.GetRecordsUseCase
import com.todayrecord.domain.util.data
import com.todayrecord.presentation.model.record.mapToItem
import com.todayrecord.presentation.screen.BaseViewModel
import com.todayrecord.presentation.util.EventFlow
import com.todayrecord.presentation.util.MutableEventFlow
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class RecordsViewModel @Inject constructor(
    savedStateHandle: SavedStateHandle,
    getRecordsUseCase: GetRecordsUseCase
) : BaseViewModel() {

    private val notifyUrl = savedStateHandle.get<Uri?>(KEY_NOTIFY_URI)

    val records = getRecordsUseCase(false)
        .map { result -> result.data!!.map { it.mapToItem() } }
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