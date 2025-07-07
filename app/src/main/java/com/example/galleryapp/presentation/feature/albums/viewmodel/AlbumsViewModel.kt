package com.example.galleryapp.presentation.feature.albums.viewmodel

import android.Manifest
import android.content.Context
import android.graphics.Bitmap
import android.media.MediaMetadataRetriever
import android.media.ThumbnailUtils
import android.net.Uri
import android.os.Build
import android.provider.MediaStore
import android.util.Log
import android.util.Size
import androidx.core.content.FileProvider
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.galleryapp.domain.media.model.Album
import com.example.galleryapp.domain.media.usecase.GetAllMediaUseCase
import com.example.galleryapp.presentation.common.Result
import com.example.galleryapp.presentation.common.util.GeneralUtility
import com.example.galleryapp.presentation.feature.albums.state.AlbumsState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import java.io.File
import java.io.FileOutputStream
import javax.inject.Inject

@HiltViewModel
class AlbumsViewModel  @Inject constructor(
    private val getAllMediaUseCase: GetAllMediaUseCase
) : ViewModel() {

    private val _navigateToAlbumDetails = MutableSharedFlow<Album>()
    val navigateToAlbumDetails = _navigateToAlbumDetails.asSharedFlow()

    private var _albumsDataState = MutableStateFlow(AlbumsState())
    val albumsDataState: StateFlow<AlbumsState> = _albumsDataState.asStateFlow()

    private val _launchPermissions = MutableSharedFlow<List<String>>()
    val launchPermissions = _launchPermissions.asSharedFlow()

    fun navToAlbumDetails(album: Album) = viewModelScope.launch {
        _navigateToAlbumDetails.emit(album)

    }

    fun getAllMedia(context: Context) {
        viewModelScope.launch {
            getAllMediaUseCase(context = context)
                .flowOn(Dispatchers.IO)
                .collect { result ->
                    when (result) {
                        is Result.Loading -> {
                            _albumsDataState.update { it.copy(isLoading = true) }
                        }
                        is Result.Success -> {
                            val grouped = result.data.groupBy { it.bucketName }
                            val allImages = result.data.filter { !it.isVideo }
                            val allVideos = result.data.filter { it.isVideo }

                            val allImagesAlbum = if (allImages.isNotEmpty()) {
                                Album(
                                    name = "All Images",
                                    coverUri = allImages.first().uri.toString(),
                                    mediaCount = allImages.size,
                                    isVideo = false
                                )
                            } else null

                            val allVideosAlbum = if (allVideos.isNotEmpty()) {
                                Album(
                                    name = "All Videos",
                                    coverUri = allVideos.first().uri.toString(),
                                    mediaCount = allVideos.size,
                                    isVideo = true
                                )
                            } else null

                            val folderAlbums = grouped.map { (folder, items) ->
                                val firstItem = items.first()
                                Album(
                                    name = folder,
                                    coverUri = firstItem.uri.toString(),
                                    mediaCount = items.size,
                                    isVideo = firstItem.isVideo
                                )
                            }.sortedByDescending { it.mediaCount }

                            val albums = listOfNotNull(allImagesAlbum, allVideosAlbum) + folderAlbums

                            _albumsDataState.update {
                                it.copy(
                                    albums = albums,
                                    isLoading = false
                                )
                            }
                        }
                        is Result.Error -> {
                            _albumsDataState.update {
                                it.copy(isLoading = false, error = "No media found  !")
                            }
                        }
                    }
                }
        }

    }
     fun checkMediaPermissionsIsGranted(context: Context, launchPermissions: Boolean) =
        viewModelScope.launch {
            val permissionsNotGranted = mutableListOf<String>()

            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
                if (!GeneralUtility.checkPermissionIsGranted(
                        context,
                        Manifest.permission.READ_MEDIA_IMAGES
                    )
                ) {
                    permissionsNotGranted.add(Manifest.permission.READ_MEDIA_IMAGES)
                }

                if (!GeneralUtility.checkPermissionIsGranted(
                        context,
                        Manifest.permission.READ_MEDIA_VIDEO
                    )
                ) {
                    permissionsNotGranted.add(Manifest.permission.READ_MEDIA_VIDEO)
                }

            } else {
                if (!GeneralUtility.checkPermissionIsGranted(
                        context,
                        Manifest.permission.READ_EXTERNAL_STORAGE
                    )
                ) {
                    permissionsNotGranted.add(Manifest.permission.READ_EXTERNAL_STORAGE)
                }
            }

            if (permissionsNotGranted.isEmpty()) {
                _albumsDataState.update {
                    it.copy(hasMediaPermission = true)
                }
                getAllMedia(context)
            } else {
                _albumsDataState.update {
                    it.copy(hasMediaPermission = false)
                }
                if (launchPermissions)
                    _launchPermissions.emit(permissionsNotGranted)
            }
        }

}