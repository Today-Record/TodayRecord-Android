package com.todayrecord.presentation.screen.recorddetail

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.viewModelScope
import com.todayrecord.domain.usecase.record.DeleteRecordUseCase
import com.todayrecord.domain.usecase.record.GetRecordUseCase
import com.todayrecord.domain.usecase.record.SetRecordDeleteUseCase
import com.todayrecord.domain.util.Result
import com.todayrecord.domain.util.data
import com.todayrecord.domain.util.succeeded
import com.todayrecord.presentation.model.record.Record
import com.todayrecord.presentation.model.record.mapToItem
import com.todayrecord.presentation.screen.BaseViewModel
import com.todayrecord.presentation.util.EventFlow
import com.todayrecord.presentation.util.MutableEventFlow
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.filterNotNull
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class RecordDetailViewModel @Inject constructor(
    savedStateHandle: SavedStateHandle,
    private val getRecordUseCase: GetRecordUseCase,
    private val setRecordDeleteUseCase: SetRecordDeleteUseCase,
    private val deleteRecordUseCase: DeleteRecordUseCase
) : BaseViewModel() {

    private val recordId = savedStateHandle.get<String>(KEY_RECORD_ID) ?: ""

    private val recordState = loadDataSignal
        .flatMapLatest { getRecordUseCase(GetRecordUseCase.Params(recordId)) }
        .stateIn(viewModelScope, SharingStarted.Lazily, Result.Loading)

    val record = recordState
        .map { if (it.succeeded) it.data!!.mapToItem() else null }
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
                    deleteRecordUseCase(DeleteRecordUseCase.Params(record.value?.id ?: ""))
                } else {
                    setRecordDeleteUseCase(SetRecordDeleteUseCase.Params(it.id, true))
                }
            }
            _navigateToBack.emit(Unit)
        }
    }

    fun navigateToRestoreAndBack() {
        viewModelScope.launch {
            record.value?.let { setRecordDeleteUseCase(SetRecordDeleteUseCase.Params(it.id, false)) }
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