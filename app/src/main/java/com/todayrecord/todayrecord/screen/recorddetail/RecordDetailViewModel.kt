package com.todayrecord.todayrecord.screen.recorddetail

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.viewModelScope
import com.todayrecord.todayrecord.data.repository.record.RecordRepository
import com.todayrecord.todayrecord.model.record.Record
import com.todayrecord.todayrecord.screen.BaseViewModel
import com.todayrecord.todayrecord.util.type.EventFlow
import com.todayrecord.todayrecord.util.type.MutableEventFlow
import com.todayrecord.todayrecord.util.type.data
import com.todayrecord.todayrecord.util.type.succeeded
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.filterNotNull
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class RecordDetailViewModel @Inject constructor(
    savedStateHandle: SavedStateHandle,
    private val recordRepository: RecordRepository
) : BaseViewModel() {

    val record = recordRepository.getRecord(savedStateHandle.get(KEY_RECORD_ID) ?: "")
        .map { if (it.succeeded) it.data!! else null }
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), null)

    val recordContentItem = record
        .filterNotNull()
        .map { record ->
            record.images
                .map { RecordDetailItemUiModel.RecordDetailImageItem(it) }
                .plus(RecordDetailItemUiModel.RecordDetailTextItem(record.content))
        }

    private val _navigateToEditDialog = MutableEventFlow<Unit>()
    val navigateToEditDialog: EventFlow<Unit> = _navigateToEditDialog

    private val _navigateToBinEditDialog = MutableEventFlow<Unit>()
    val navigateToBinEditDialog: EventFlow<Unit> = _navigateToBinEditDialog

    private val _navigateToRecordEdit = MutableEventFlow<Record?>()
    val navigateToRecordEdit: EventFlow<Record?> = _navigateToRecordEdit

    private val _navigateToBack = MutableEventFlow<Unit>()
    val navigateToBack: EventFlow<Unit> = _navigateToBack

    fun navigateToRecordDetailEditDialog() {
        viewModelScope.launch {
            if (record.value?.isDeleted == true) {
                _navigateToBinEditDialog.emit(Unit)
            } else {
                _navigateToEditDialog.emit(Unit)
            }
        }
    }

    fun navigateToRecordEdit() {
        viewModelScope.launch {
            _navigateToRecordEdit.emit(record.value)
        }
    }

    fun navigateToDeletedAndBack() {
        viewModelScope.launch {
            record.value?.let {
                if (it.isDeleted) {
                    recordRepository.deleteRecord(record.value?.id ?: "")
                } else {
                    recordRepository.setRecordDelete(it.id, true)
                }
            }
            _navigateToBack.emit(Unit)
        }
    }

    fun navigateToRestoreAndBack() {
        viewModelScope.launch {
            record.value?.let { recordRepository.setRecordDelete(it.id, false) }
            _navigateToBack.emit(Unit)
        }
    }


    companion object {
        private const val KEY_RECORD_ID = "record_id"
    }
}

sealed class RecordDetailItemUiModel {
    data class RecordDetailImageItem(val imageUrl: String) : RecordDetailItemUiModel()
    data class RecordDetailTextItem(val text: String?) : RecordDetailItemUiModel()
}