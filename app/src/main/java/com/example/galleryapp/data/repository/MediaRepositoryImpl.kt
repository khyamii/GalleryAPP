package com.example.galleryapp.data.repository

import android.content.ContentUris
import android.content.Context
import android.provider.MediaStore
import com.example.galleryapp.domain.media.model.MediaItem
import com.example.galleryapp.domain.media.repository.MediaRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import com.example.galleryapp.presentation.common.Result
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.flowOn


class MediaRepositoryImpl : MediaRepository {
    override fun getAllMedia(context: Context): Flow<Result<List<MediaItem>>> = flow {
        emit(Result.Loading())

        try {
            val mediaList = mutableListOf<MediaItem>()
            val projection = arrayOf(
                MediaStore.Files.FileColumns._ID,
                MediaStore.Files.FileColumns.DISPLAY_NAME,
                MediaStore.Files.FileColumns.DATE_ADDED,
                MediaStore.Files.FileColumns.MIME_TYPE,
                MediaStore.Files.FileColumns.BUCKET_DISPLAY_NAME,
                MediaStore.Files.FileColumns.DATA
            )

            val selection =
                "${MediaStore.Files.FileColumns.MEDIA_TYPE}=? OR ${MediaStore.Files.FileColumns.MEDIA_TYPE}=?"
            val selectionArgs = arrayOf(
                MediaStore.Files.FileColumns.MEDIA_TYPE_IMAGE.toString(),
                MediaStore.Files.FileColumns.MEDIA_TYPE_VIDEO.toString()
            )

            val uri = MediaStore.Files.getContentUri("external")

            context.contentResolver.query(uri, projection, selection, selectionArgs, null)
                ?.use { cursor ->
                    val idCol = cursor.getColumnIndexOrThrow(MediaStore.Files.FileColumns._ID)
                    val nameCol =
                        cursor.getColumnIndexOrThrow(MediaStore.Files.FileColumns.DISPLAY_NAME)
                    val dateCol =
                        cursor.getColumnIndexOrThrow(MediaStore.Files.FileColumns.DATE_ADDED)
                    val mimeCol =
                        cursor.getColumnIndexOrThrow(MediaStore.Files.FileColumns.MIME_TYPE)
                    val bucketCol =
                        cursor.getColumnIndexOrThrow(MediaStore.Files.FileColumns.BUCKET_DISPLAY_NAME)
                    val dataCol = cursor.getColumnIndexOrThrow(MediaStore.Files.FileColumns.DATA)

                    while (cursor.moveToNext()) {
                        val path = cursor.getString(dataCol)?.lowercase() ?: continue
                        if (path.contains("cache") || path.contains("thumb") || path.contains(".nomedia")) continue

                        val id = cursor.getLong(idCol)
                        val uriItem = ContentUris.withAppendedId(uri, id)

                        val mimeType = cursor.getString(mimeCol) ?: ""
                        val isVideo = mimeType.startsWith("video")

                        mediaList.add(
                            MediaItem(
                                uri = uriItem,
                                name = cursor.getString(nameCol) ?: "",
                                dateAdded = cursor.getLong(dateCol),
                                mimeType = mimeType,
                                bucketName = cursor.getString(bucketCol) ?: "Unknown",
                                isVideo = isVideo
                            )
                        )
                    }
                }

            emit(Result.Success(mediaList.toList()))
        } catch (e: Exception) {
            emit(Result.Error(e.localizedMessage ?: "Unexpected Error"))
        }
    }
}