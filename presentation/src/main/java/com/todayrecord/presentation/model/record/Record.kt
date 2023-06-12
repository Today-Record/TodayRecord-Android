package com.todayrecord.presentation.model.record

import android.os.Parcelable
import com.todayrecord.domain.usecase.record.entity.RecordEntity
import kotlinx.parcelize.Parcelize

/*
{
	“id” : “550e8400-e29b-41d4-a716-446655440000”, // UUID가 들어갑니다.
	“date” : “2023-03-01T00:00:00”,
	“content” : “오늘은 하루종일 비가 내렸다. 우산을 챙겨서 다행이다.”,
	“images” : [“https://imageUrl/1qwer23”, “https://imageUrl/1qwer24”],
	“isDeleted” : false,
	“createAt”:  “2023-03-01T00:00:00”,
	“updateAt”:  “2023-03-01T00:00:00”,
}
 */

@Parcelize
data class Record(
    val id: String,
    val date: String,
    val content: String?,
    val images: List<String>,
    val isDeleted: Boolean,
    val createdAt: String,
    val updatedAt: String
) : Parcelable

fun RecordEntity.mapToItem(): Record {
    return Record(
        id = id,
        date = date,
        content = content,
        images = images,
        isDeleted = isDeleted,
        createdAt = createdAt,
        updatedAt = updatedAt
    )
}

fun Record.mapToEntity(): RecordEntity {
    return RecordEntity(
        id = id,
        date = date,
        content = content,
        images = images,
        isDeleted = isDeleted,
        createdAt = createdAt,
        updatedAt = updatedAt
    )
}