package com.todayrecord.todayrecord.screen.intro

import android.net.Uri
import androidx.core.net.toUri
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.viewModelScope
import com.todayrecord.todayrecord.screen.BaseViewModel
import com.todayrecord.todayrecord.util.type.EventFlow
import com.todayrecord.todayrecord.util.type.MutableEventFlow
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class IntroViewModel @Inject constructor(
    savedStateHandle: SavedStateHandle
) : BaseViewModel() {

    private val notifyUri = savedStateHandle.get<String?>(KEY_NOTIFY_URI)

    private val _navigateToRecords = MutableEventFlow<Uri?>()
    val navigateToRecords: EventFlow<Uri?> = _navigateToRecords

    fun navigateToRecords() {
        viewModelScope.launch {
            _navigateToRecords.emit(notifyUri?.toUri())
        }
    }

    companion object {
        private const val KEY_NOTIFY_URI = "notify_uri"
    }
}