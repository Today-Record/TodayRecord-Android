package com.todayrecord.todayrecord.screen

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.todayrecord.todayrecord.util.type.EventFlow
import com.todayrecord.todayrecord.util.type.MutableEventFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

open class BaseViewModel : ViewModel() {

    private val _loading = MutableStateFlow<Boolean>(false)
    val loading: StateFlow<Boolean> = _loading

    private val _errorMessage = MutableEventFlow<String>()
    val errorMessage: EventFlow<String> = _errorMessage

    fun setLoading(isLoading: Boolean) {
        _loading.value = isLoading
    }

    fun setErrorMessage(message: String) {
        viewModelScope.launch {
            _errorMessage.emit(message)
        }
    }
}