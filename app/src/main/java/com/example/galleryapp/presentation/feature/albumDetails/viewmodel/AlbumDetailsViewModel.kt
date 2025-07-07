package com.example.galleryapp.presentation.feature.albumDetails.viewmodel

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.galleryapp.domain.media.usecase.GetAllMediaUseCase
import com.example.galleryapp.presentation.common.Result
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import com.example.galleryapp.presentation.feature.albumDetails.state.AlbumDetailsState
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject



@HiltViewModel
class AlbumDetailsViewModel @Inject constructor(
    private val getAllMediaUseCase: GetAllMediaUseCase
) : ViewModel() {

    private var _albumDetailsState = MutableStateFlow(AlbumDetailsState())
    val albumDetailsState: StateFlow<AlbumDetailsState> = _albumDetailsState.asStateFlow()

    private val _navigateBack = MutableSharedFlow<Boolean>()
    val navigateBack = _navigateBack.asSharedFlow()

    fun navBack() = viewModelScope.launch {
        _navigateBack.emit(true)

    }


    fun loadMediaForAlbum(context: Context, albumName: String) {
        viewModelScope.launch {
            getAllMediaUseCase(context = context)
                .flowOn(Dispatchers.IO)
                .collect { result ->
                    when (result) {
                        is Result.Loading -> {
                            _albumDetailsState.update { it.copy(isLoading = true) }
                        }
                        is Result.Success -> {

                            val mediaList = when (albumName) {
                                "All Images" -> result.data.filter { !it.isVideo }
                                "All Videos" -> result.data.filter { it.isVideo }
                                else -> result.data.filter { it.bucketName == albumName }
                            }

                            _albumDetailsState.update {
                                it.copy(
                                    allMedia = mediaList,
                                    isLoading = false
                                )
                            }

                        }
                        is Result.Error -> {
                            _albumDetailsState.update {
                                it.copy(isLoading = false, error = "No media found  !")
                            }
                        }
                    }
                }
        }
    }

}