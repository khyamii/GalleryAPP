package com.example.galleryapp.domain.media.usecase

import android.content.Context
import com.example.galleryapp.domain.media.repository.MediaRepository
import com.example.galleryapp.domain.media.model.MediaItem
import com.example.galleryapp.presentation.common.Result
import kotlinx.coroutines.flow.Flow

class GetAllMediaUseCase(
    private val mediaRepository: MediaRepository
) {
    operator fun invoke(context: Context): Flow<Result<List<MediaItem>>> {
        return mediaRepository.getAllMedia(context)
    }
}