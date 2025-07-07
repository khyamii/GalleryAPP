package com.example.galleryapp.presentation.feature.albumDetails.state

import com.example.galleryapp.domain.media.model.MediaItem

data class AlbumDetailsState(
    val allMedia: List<MediaItem> = emptyList(),
    val isLoading: Boolean = false,
    val error: String? = null

    )
