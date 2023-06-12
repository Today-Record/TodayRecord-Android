package com.todayrecord.presentation.model.media

import android.net.Uri
import android.os.Parcelable
import com.todayrecord.domain.usecase.media.entity.MediaEntity
import kotlinx.parcelize.Parcelize

@Parcelize
data class Media(
    val uri: Uri,
    val dateTimeMills: Long,
    val selectedPosition : Int? = null // use only presentation-layer
) : Parcelable

fun MediaEntity.mapToItem() : Media {
    return Media(
        uri = Uri.parse(this.uri),
        dateTimeMills = dateTimeMills
    )
}