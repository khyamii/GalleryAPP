package com.example.galleryapp.presentation.feature.albumDetails.view

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.lazy.grid.rememberLazyGridState
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavController
import com.example.disastermed.common.extensions.collectAsEffect
import com.example.galleryapp.domain.media.model.Album
import com.example.galleryapp.presentation.common.component.AppTopBar
import com.example.galleryapp.presentation.common.component.MediaCard
import com.example.galleryapp.presentation.common.ui.theme.AppTheme
import com.example.galleryapp.presentation.feature.albumDetails.state.AlbumDetailsState
import com.example.galleryapp.presentation.feature.albumDetails.viewmodel.AlbumDetailsViewModel

@Composable
fun AlbumDetailsScreen(
    albumData: Album,
    navController: NavController,
    viewModel: AlbumDetailsViewModel = hiltViewModel()
) {
    val context = LocalContext.current
    val state by viewModel.albumDetailsState.collectAsStateWithLifecycle()

    AlbumDetailsDataContent(albumData, state, viewModel)

    if (state.allMedia.isEmpty()) {
        LaunchedEffect(Unit) {
            viewModel.loadMediaForAlbum(context, albumData.name)
        }
    }

    viewModel.navigateBack.collectAsEffect {
        navController.popBackStack()
    }
}
@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun AlbumDetailsDataContent(
    albumData: Album,
    state: AlbumDetailsState,
    viewModel: AlbumDetailsViewModel
) {
    val gridState = rememberLazyGridState()

    Column(modifier = Modifier.fillMaxSize()) {
        AppTopBar(
            title = albumData.name,
            subtitle = "${albumData.mediaCount}, items",
            showBackButton = true,
            onBackButtonClick = { viewModel.navBack() }
        )

        Spacer(Modifier.height(8.dp))

        when {
            state.isLoading -> {
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(top = 16.dp),
                    contentAlignment = Alignment.TopCenter
                ) {
                    CircularProgressIndicator(color = AppTheme.colors.secondaryDefaultLight)
                }
            }

            state.allMedia.isEmpty() -> {
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(top = 32.dp),
                    contentAlignment = Alignment.TopCenter
                ) {
                    Text(
                        text = "No Media available",
                        color = AppTheme.colors.secondaryDefaultLight,
                        style = MaterialTheme.typography.bodyLarge
                    )
                }
            }

            else -> {
                LazyVerticalGrid(
                    state = gridState,
                    columns = GridCells.Fixed(3),
                    verticalArrangement = Arrangement.spacedBy(8.dp),
                    horizontalArrangement = Arrangement.spacedBy(8.dp),
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(horizontal = 12.dp, vertical = 8.dp)
                ) {
                    items(state.allMedia, key = { it.name }) { media ->
                        MediaCard(
                            mediaItem = media,
                            modifier = Modifier
                                .fillMaxWidth()
                                .aspectRatio(1f)
                        ) { /* onClick */ }
                    }
                }
            }
        }
    }
}

@Preview
@Composable
fun AlbumDetailsPreview() {
    AppTheme {
        AlbumDetailsDataContent(
            albumData = Album("Test Album", "", 3),
            state = AlbumDetailsState(),
            viewModel = hiltViewModel()
        )
    }
}