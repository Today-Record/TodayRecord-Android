package com.todayrecord.data.source.repository.media

import android.content.ContentResolver
import android.content.ContentUris
import android.content.Context
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.provider.MediaStore
import androidx.core.content.ContentResolverCompat
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import androidx.paging.PagingSource
import androidx.paging.PagingState
import com.todayrecord.domain.usecase.media.MediaRepository
import com.todayrecord.domain.usecase.media.entity.MediaEntity
import com.todayrecord.domain.util.Result
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import timber.log.Timber
import javax.inject.Inject

internal class MediaRepositoryImpl @Inject constructor(
    @ApplicationContext private val context: Context
) : MediaRepository {

    private val cursorProjection: Array<String> = arrayOf(MediaStore.Images.Media._ID, MediaStore.Images.Media.DATA, MediaStore.Images.Media.BUCKET_DISPLAY_NAME, MediaStore.Images.Media.DATE_ADDED)
    private val cursorUri: Uri = MediaStore.Images.Media.EXTERNAL_CONTENT_URI
    private val cursorSortOrder: String = String.format("%s %s", MediaStore.Images.Media.DATE_MODIFIED, "DESC")
    private val cursorSelection: String = MediaStore.Images.Media.SIZE + " > ? "
    private val cursorSelectionArgs: Array<String> = arrayOf("0")

    override fun getMedias(): Flow<Result<PagingData<MediaEntity>>> = Pager(
        config = PagingConfig(pageSize = MEDIA_PAGE_SIZE, enablePlaceholders = false)
    ) {
        object : PagingSource<Int, MediaEntity>() {
            override fun getRefreshKey(state: PagingState<Int, MediaEntity>): Int? = null

            override suspend fun load(params: LoadParams<Int>): LoadResult<Int, MediaEntity> {
                val offset = params.key ?: 0

                return try {
                    if (Build.VERSION.SDK_INT > Build.VERSION_CODES.Q) {
                        context.contentResolver.query(
                            cursorUri,
                            cursorProjection,
                            Bundle().apply {
                                // Sort function
                                putStringArray(
                                    ContentResolver.QUERY_ARG_SORT_COLUMNS,
                                    arrayOf(MediaStore.Images.Media.DATE_MODIFIED)
                                )
                                putInt(
                                    ContentResolver.QUERY_ARG_SORT_DIRECTION,
                                    ContentResolver.QUERY_SORT_DIRECTION_DESCENDING
                                )
                                // Limit & Offset
                                putInt(ContentResolver.QUERY_ARG_LIMIT, params.loadSize)
                                putInt(ContentResolver.QUERY_ARG_OFFSET, offset)

                                // Selection
                                putString(
                                    ContentResolver.QUERY_ARG_SQL_SELECTION,
                                    cursorSelection
                                )
                                putStringArray(
                                    ContentResolver.QUERY_ARG_SQL_SELECTION_ARGS,
                                    cursorSelectionArgs
                                )
                            },
                            null
                        )
                    } else {
                        ContentResolverCompat.query(
                            context.contentResolver,
                            cursorUri,
                            cursorProjection,
                            cursorSelection,
                            cursorSelectionArgs,
                            cursorSortOrder + " LIMIT ${params.loadSize} OFFSET $offset",
                            null
                        )
                    }?.use { cursor ->
                        val medias = generateSequence { if (cursor.moveToNext()) cursor else null }
                            .map {
                                it.run {
                                    val mediaUri: Uri = ContentUris.withAppendedId(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, getLong(getColumnIndexOrThrow(MediaStore.Images.Media._ID)))
                                    val dateTimeMills = getLong(getColumnIndexOrThrow(MediaStore.Images.Media.DATE_ADDED)).times(1000)
                                    MediaEntity(mediaUri.toString(), dateTimeMills)
                                }
                            }
                            .toList()

                        LoadResult.Page(
                            data = medias,
                            prevKey = null,
                            nextKey = if (medias.size == params.loadSize) offset.plus(params.loadSize) else null

                        )
                    } ?: LoadResult.Error(NullPointerException())
                } catch (exception: Exception) {
                    Timber.e(exception, "[getMedias] message : ${exception.message}")
                    LoadResult.Error(exception)
                }
            }
        }
    }.flow.map { Result.Success(it) }

    companion object {
        private const val MEDIA_PAGE_SIZE = 50
    }
}