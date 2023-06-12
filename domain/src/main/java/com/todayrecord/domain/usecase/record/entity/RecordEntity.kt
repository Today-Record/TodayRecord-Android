package com.todayrecord.domain.usecase.record.entity

import com.todayrecord.domain.usecase.Entity

data class RecordEntity(
    val id: String,
    val date: String,
    val content: String?,
    val images: List<String>,
    val isDeleted: Boolean,
    val createdAt: String,
    val updatedAt: String
) : Entity