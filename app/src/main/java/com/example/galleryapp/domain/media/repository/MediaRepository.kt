package com.example.galleryapp.domain.media.repository

import android.content.Context
import com.example.galleryapp.domain.media.model.MediaItem
import com.example.galleryapp.presentation.common.Result
import kotlinx.coroutines.flow.Flow

interface MediaRepository {
     fun getAllMedia(context: Context): Flow<Result<List<MediaItem>>>

}