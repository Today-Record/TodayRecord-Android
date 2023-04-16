package com.todayrecord.todayrecord.screen.intro

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.viewModelScope
import com.todayrecord.todayrecord.screen.BaseViewModel
import com.todayrecord.todayrecord.util.EventFlow
import com.todayrecord.todayrecord.util.MutableEventFlow
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class IntroViewModel @Inject constructor(
    savedStateHandle: SavedStateHandle
) : BaseViewModel() {

    private val _navigateToRecords = MutableEventFlow<Unit>()
    val navigateToRecords: EventFlow<Unit> = _navigateToRecords

    fun navigateToRecords() {
        viewModelScope.launch {
            _navigateToRecords.emit(Unit)
        }
    }
}