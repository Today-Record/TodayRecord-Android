package com.todayrecord.todayrecord.model.media

import android.net.Uri
import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class Media(
    val uri: Uri,
    val dateTimeMills: Long,
    val selectedPosition : Int? = null // use only ui-layer
) : Parcelable