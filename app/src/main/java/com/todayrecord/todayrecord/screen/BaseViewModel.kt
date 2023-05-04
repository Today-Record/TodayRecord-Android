package com.todayrecord.todayrecord.screen

import android.net.Uri
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.todayrecord.todayrecord.util.type.EventFlow
import com.todayrecord.todayrecord.util.type.MutableEventFlow
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.emitAll
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.launch

open class BaseViewModel : ViewModel() {

    // Used to re-run flows on command
    protected val refreshSignal = MutableSharedFlow<Unit>()

    // Used to run flows on init and also on command
    protected val loadDataSignal: Flow<Unit> = flow {
        emit(Unit)
        emitAll(refreshSignal)
    }

    private val _loading = MutableStateFlow<Boolean>(false)
    val loading: StateFlow<Boolean> = _loading

    private val _errorMessage = MutableEventFlow<String>()
    val errorMessage: EventFlow<String> = _errorMessage

    private val _navigateToDeepLink = MutableEventFlow<Uri>()
    val navigateToDeepLink: EventFlow<Uri> = _navigateToDeepLink

    fun setLoading(isLoading: Boolean) {
        _loading.value = isLoading
    }

    fun setErrorMessage(message: String) {
        viewModelScope.launch {
            _errorMessage.emit(message)
        }
    }

    fun navigateToDeepLink(uri: Uri) {
        viewModelScope.launch {
            _navigateToDeepLink.emit(uri)
        }
    }

    open fun onRefresh() = viewModelScope.launch {
        refreshSignal.emit(Unit)
    }

}