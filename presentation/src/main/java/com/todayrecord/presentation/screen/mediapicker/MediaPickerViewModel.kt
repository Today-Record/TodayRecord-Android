package com.todayrecord.presentation.screen.mediapicker

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.viewModelScope
import androidx.paging.PagingData
import androidx.paging.cachedIn
import androidx.paging.map
import com.todayrecord.domain.usecase.media.GetMediaUseCase
import com.todayrecord.domain.util.data
import com.todayrecord.presentation.model.media.Media
import com.todayrecord.presentation.model.media.mapToItem
import com.todayrecord.presentation.screen.BaseViewModel
import com.todayrecord.presentation.util.EventFlow
import com.todayrecord.presentation.util.MutableEventFlow
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.filterNotNull
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MediaPickerViewModel @Inject constructor(
    private val savedStateHandle: SavedStateHandle,
    private val getMediaUseCase: GetMediaUseCase
) : BaseViewModel() {

    private val selectMaxCount: Int
        get() = savedStateHandle.get(KEY_MEDIA_MAX_COUNT) ?: 0

    val selectedMediaPaths: StateFlow<List<String>> = savedStateHandle
        .getStateFlow(KEY_SELECTED_MEDIA_PATHS, emptyList())

    private val _mediaPermission = MutableStateFlow<Boolean?>(null)
    val mediaPermission: StateFlow<Boolean?> = _mediaPermission

    val medias = mediaPermission.filterNotNull()
        .flatMapLatest {
            if (it) {
                getMediaUseCase(Unit).map { result -> result.data!!.map { it.mapToItem() } }
            } else {
                flowOf(PagingData.empty())
            }
        }
        .cachedIn(viewModelScope)
        .combine(selectedMediaPaths) { mediaData: PagingData<Media>, selectedPaths: List<String> ->
            mediaData.map { media ->
                selectedPaths.indexOf(media.uri.toString()).let {
                    media.copy(selectedPosition = if (it == -1) null else it.plus(1))
                }
            }
        }

    private val _navigateToBack = MutableEventFlow<List<String>>()
    val navigateToBack: EventFlow<List<String>> = _navigateToBack

    fun initializeMediaPermission(isApprove: Boolean) {
        if (mediaPermission.value != null) return
        _mediaPermission.value = isApprove
    }

    fun setSelectedMedias(mediaPath: String) {
        savedStateHandle.set(KEY_SELECTED_MEDIA_PATHS,
            selectedMediaPaths.value
                .toMutableList()
                .apply {
                    if (any { it == mediaPath }) {
                        removeAll { it == mediaPath }
                    } else {
                        if (size >= selectMaxCount) {
                            setErrorMessage("현재 ${selectedMediaPaths.value.size}개까지만 선택 할 수 있습니다")
                        } else {
                            add(mediaPath)
                        }
                    }
                }
        )
    }

    fun navigateToBack() {
        viewModelScope.launch {
            if (selectedMediaPaths.value.isEmpty()) {
                setErrorMessage("사진을 1장 이상 선택해주세요")
            } else {
                _navigateToBack.emit(selectedMediaPaths.value)
            }
        }
    }

    companion object {
        private const val KEY_SELECTED_MEDIA_PATHS = "selected_media_paths"
        private const val KEY_MEDIA_MAX_COUNT = "media_max_count"
    }
}