package com.example.galleryapp.domain.media.model

import android.net.Uri

data class MediaItem(
    val uri: Uri,
    val name: String,
    val dateAdded: Long,
    val mimeType: String,
    val bucketName: String,
    val isVideo: Boolean
)