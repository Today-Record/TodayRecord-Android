package com.todayrecord.data.source.repository.record.model

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.todayrecord.data.source.Model
import com.todayrecord.domain.usecase.record.entity.RecordEntity

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
@Entity(tableName = "record")
data class Record(
    @PrimaryKey
    val id: String,
    @ColumnInfo(name = "date")
    val date: String,
    @ColumnInfo(name = "content")
    val content: String?,
    @ColumnInfo(name = "images")
    val images: List<String>,
    @ColumnInfo(name = "isDeleted")
    val isDeleted: Boolean,
    @ColumnInfo(name = "createdAt")
    val createdAt: String,
    @ColumnInfo(name = "updatedAt")
    val updatedAt: String
) : Model

internal fun RecordEntity.mapToModel(): Record {
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

internal fun Record.mapToEntity(): RecordEntity {
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
