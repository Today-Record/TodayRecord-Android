package com.todayrecord.presentation.screen.intro

import android.net.Uri
import androidx.core.net.toUri
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.viewModelScope
import com.todayrecord.presentation.screen.BaseViewModel
import com.todayrecord.presentation.util.EventFlow
import com.todayrecord.presentation.util.MutableEventFlow
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