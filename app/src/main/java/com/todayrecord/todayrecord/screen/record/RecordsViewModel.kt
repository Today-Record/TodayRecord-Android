package com.todayrecord.todayrecord.screen.record

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.viewModelScope
import com.todayrecord.todayrecord.screen.BaseViewModel
import com.todayrecord.todayrecord.util.EventFlow
import com.todayrecord.todayrecord.util.MutableEventFlow
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class RecordsViewModel @Inject constructor(
    savedStateHandle: SavedStateHandle
) : BaseViewModel() {

    private val _navigateToWriteRecord = MutableEventFlow<Unit>()
    val navigateToWriteRecord: EventFlow<Unit> = _navigateToWriteRecord

    fun navigateToWriteRecord() {
        viewModelScope.launch {
            _navigateToWriteRecord.emit(Unit)
        }
    }
}