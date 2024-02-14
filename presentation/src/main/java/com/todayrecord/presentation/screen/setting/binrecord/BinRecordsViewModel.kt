package com.todayrecord.presentation.screen.setting.binrecord

import androidx.lifecycle.viewModelScope
import androidx.paging.cachedIn
import androidx.paging.insertSeparators
import androidx.paging.map
import com.todayrecord.domain.usecase.record.ClearBinRecordsUseCase
import com.todayrecord.domain.usecase.record.GetRecordsUseCase
import com.todayrecord.domain.util.data
import com.todayrecord.presentation.model.record.Record
import com.todayrecord.presentation.model.record.mapToItem
import com.todayrecord.presentation.screen.base.BaseViewModel
import com.todayrecord.presentation.util.EventFlow
import com.todayrecord.presentation.util.MutableEventFlow
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class BinRecordsViewModel @Inject constructor(
    getRecordsUseCase: GetRecordsUseCase,
    private val clearBinRecordsUseCase: ClearBinRecordsUseCase
) : BaseViewModel() {

    val binRecords = getRecordsUseCase(true)
        .map { result -> result.data!!.map { BinRecordsUiModel.BinRecordItem(it.mapToItem()) } }
        .map { pagingData ->
            pagingData.insertSeparators { before: BinRecordsUiModel?, after: BinRecordsUiModel? ->
                if (before == null && after == null) {
                    return@insertSeparators BinRecordsUiModel.EmptyItem
                }
                null
            }
        }
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
            clearBinRecordsUseCase(Unit)
        }
    }
}

sealed class BinRecordsUiModel {
    data class BinRecordItem(val record: Record) : BinRecordsUiModel()
    data object EmptyItem : BinRecordsUiModel()
}