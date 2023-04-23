package com.todayrecord.todayrecord.screen.write

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.viewModelScope
import com.todayrecord.todayrecord.data.repository.record.RecordRepository
import com.todayrecord.todayrecord.model.record.Record
import com.todayrecord.todayrecord.screen.BaseViewModel
import com.todayrecord.todayrecord.util.EventFlow
import com.todayrecord.todayrecord.util.MutableEventFlow
import com.todayrecord.todayrecord.util.Result
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch
import java.time.ZoneId
import java.time.ZonedDateTime
import java.util.UUID
import javax.inject.Inject

@HiltViewModel
class WriteRecordViewModel @Inject constructor(
    private val savedStateHandle: SavedStateHandle,
    private val recordRepository: RecordRepository
) : BaseViewModel() {

    val record = savedStateHandle.get<Record?>(KEY_RECORD)
    val recordText: StateFlow<String?> = savedStateHandle.getStateFlow(KEY_RECORD_TEXT, record?.content)
    val recordDate: StateFlow<String> = savedStateHandle.getStateFlow(KEY_RECORD_DATE, record?.date ?: ZonedDateTime.now().toString())
    val recordImages: StateFlow<List<String>> = savedStateHandle.getStateFlow(KEY_RECORD_IMAGES, record?.images ?: emptyList())

    private val _isRecordSaveEnable = MutableStateFlow<Boolean>(false)
    val isRecordSaveEnable: StateFlow<Boolean> = _isRecordSaveEnable

    private val _navigateToMediaPicker = MutableEventFlow<Int>()
    val navigateToMediaPicker: EventFlow<Int> = _navigateToMediaPicker

    private val _navigateToDatePicker = MutableEventFlow<ZonedDateTime>()
    val navigateToDatePicker: EventFlow<ZonedDateTime> = _navigateToDatePicker

    private val _navigateToTimePicker = MutableEventFlow<ZonedDateTime>()
    val navigateToTimePicker: EventFlow<ZonedDateTime> = _navigateToTimePicker

    private val _navigateToBack = MutableEventFlow<Unit>()
    val navigateToBack: EventFlow<Unit> = _navigateToBack

    fun existRecordText(text: String) {
        savedStateHandle.set(KEY_RECORD_TEXT, text)
        _isRecordSaveEnable.value = text.trim().isNotEmpty()
    }

    fun updateRecordDate(year: Int, month: Int, day: Int) {
        savedStateHandle.set(KEY_RECORD_DATE, ZonedDateTime.parse(recordDate.value).withYear(year).withMonth(month).withDayOfMonth(day).toString())
    }

    fun updateRecordTime(hour: Int, minute: Int) {
        savedStateHandle.set(KEY_RECORD_DATE, ZonedDateTime.parse(recordDate.value).withHour(hour).withMinute(minute).toString())
    }

    fun setRecordImages(images: List<String>) {
        savedStateHandle.set(
            KEY_RECORD_IMAGES,
            recordImages.value
                .toMutableList()
                .apply { addAll(images) }
        )
    }

    fun deleteRecordImages(image: String) {
        savedStateHandle.set(
            KEY_RECORD_IMAGES,
            recordImages.value
                .toMutableList()
                .apply { remove(image) }
                .toList()
        )
    }

    fun saveRecord() {
        viewModelScope.launch {
            val saveDate = ZonedDateTime.now().withZoneSameInstant(ZoneId.systemDefault()).toString()

            recordRepository.setRecord(
                record?.copy(
                    date = recordDate.value,
                    content = recordText.value,
                    images = recordImages.value,
                    updatedAt = saveDate
                ) ?: run {
                    Record(
                        id = UUID.randomUUID().toString(),
                        date = recordDate.value,
                        content = recordText.value,
                        images = recordImages.value,
                        isDeleted = false,
                        createdAt = saveDate,
                        updatedAt = saveDate
                    )
                }
            ).onEach {
                setLoading(it is Result.Loading)
            }.collect {
                when (it) {
                    is Result.Loading -> return@collect
                    is Result.Success -> navigateToBack()
                    is Result.Error -> setErrorMessage("기록 저장에 실패했습니다.\n 잠시 후 다시 시도해주세요.")
                }
            }
        }
    }

    fun navigateToBack() {
        viewModelScope.launch {
            _navigateToBack.emit(Unit)
        }
    }

    fun navigateToMediaPicker() {
        viewModelScope.launch {
            _navigateToMediaPicker.emit(IMAGE_MAX_COUNT - recordImages.value.size)
        }
    }

    fun navigateToDatePicker() {
        viewModelScope.launch {
            _navigateToDatePicker.emit(ZonedDateTime.parse(recordDate.value).withZoneSameInstant(ZoneId.systemDefault()))
        }
    }

    fun navigateToTimePicker() {
        viewModelScope.launch {
            _navigateToTimePicker.emit(ZonedDateTime.parse(recordDate.value).withZoneSameInstant(ZoneId.systemDefault()))
        }
    }

    companion object {
        private const val KEY_RECORD = "record"
        private const val KEY_RECORD_TEXT = "record_text"
        private const val KEY_RECORD_DATE = "record_date"
        private const val KEY_RECORD_IMAGES = "record_images"

        private const val IMAGE_MAX_COUNT = 3
    }
}