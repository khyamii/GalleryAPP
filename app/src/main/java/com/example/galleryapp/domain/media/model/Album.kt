package com.example.galleryapp.domain.media.model

import android.net.Uri

import kotlinx.serialization.Serializable
@Serializable
data class Album(
    val name: String,
    val coverUri: String,
    val mediaCount: Int,
    val isVideo: Boolean = false
)