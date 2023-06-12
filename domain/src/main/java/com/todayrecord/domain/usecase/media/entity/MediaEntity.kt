package com.todayrecord.domain.usecase.media.entity

import com.todayrecord.domain.usecase.Entity

data class MediaEntity(
    val uri: String,
    val dateTimeMills: Long
) : Entity