package com.example.galleryapp.presentation.feature.albums.state

import com.example.galleryapp.domain.media.model.Album


data class AlbumsState(
    val isLoading: Boolean = false,
    val albums: List<Album> = emptyList(),
    val error: String? = null,
    val hasMediaPermission: Boolean = false
    )
