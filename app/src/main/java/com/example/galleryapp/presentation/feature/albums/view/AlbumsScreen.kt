package com.example.galleryapp.presentation.feature.albums.view

import android.content.Context
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavController
import com.example.galleryapp.presentation.common.ui.theme.AppTheme
import com.example.galleryapp.presentation.feature.albums.state.AlbumsState
import com.example.galleryapp.presentation.feature.albums.viewmodel.AlbumsViewModel
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.grid.rememberLazyGridState
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.PermMedia
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.graphics.vector.rememberVectorPainter
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import com.example.disastermed.common.extensions.collectAsEffect
import com.example.galleryapp.R
import com.example.galleryapp.presentation.common.component.MediaAlbumCard
import com.example.galleryapp.presentation.common.component.PermissionCard
import com.example.galleryapp.presentation.common.component.PrimaryButton
import com.example.galleryapp.presentation.navigation.Routes
import androidx.compose.material.icons.filled.GridView
import androidx.compose.material.icons.filled.ViewList
import androidx.compose.material3.Icon
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import com.example.galleryapp.presentation.common.enums.AlbumViewMode

@Composable
fun AlbumsScreen(
    navController: NavController,
    viewModel: AlbumsViewModel = hiltViewModel()
) {
    val albumsState by viewModel.albumsDataState.collectAsStateWithLifecycle()
    val context = LocalContext.current
    val viewMode = rememberSaveable { mutableStateOf(AlbumViewMode.GRID) }

    val permissionLauncher = rememberLauncherForActivityResult(
        ActivityResultContracts.RequestMultiplePermissions()
    ) {
        viewModel.checkMediaPermissionsIsGranted(context, false)
    }

    viewModel.launchPermissions.collectAsEffect {
        permissionLauncher.launch(it.toTypedArray())
    }

    viewModel.navigateToAlbumDetails.collectAsEffect { album ->
        navController.navigate(Routes.AlbumsDetails(albumData = album))
    }

    LaunchedEffect(Unit) {
        if (albumsState.albums.isEmpty()) {
            viewModel.checkMediaPermissionsIsGranted(context, launchPermissions = false)
        }
    }

    AlbumsDataContent(
        context = context,
        albumsState = albumsState,
        viewModel = viewModel,
        viewMode = viewMode
    )
}
@Composable
private fun AlbumsDataContent(
    context: Context,
    albumsState: AlbumsState,
    viewModel: AlbumsViewModel,
    viewMode: MutableState<AlbumViewMode>
) {
    val gridState = rememberLazyGridState()
    val listState = rememberLazyListState()

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(8.dp)
    ) {
        Spacer(Modifier.height(10.dp))

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = 8.dp)
        ) {
            Image(
                painter = painterResource(R.drawable.app_icon),
                contentDescription = null,
                modifier = Modifier.size(45.dp).padding(8.dp)
            )
            Spacer(Modifier.weight(1f))
            if (albumsState.albums.isNotEmpty()) {
                IconButton(onClick = {
                    viewMode.value = if (viewMode.value == AlbumViewMode.GRID) AlbumViewMode.LIST else AlbumViewMode.GRID
                }) {
                    Icon(
                        imageVector = if (viewMode.value == AlbumViewMode.GRID) Icons.Default.ViewList else Icons.Default.GridView,
                        tint = AppTheme.colors.secondaryDefaultLight,
                        contentDescription = null
                    )
                }
            }
        }

        Box(modifier = Modifier.weight(1f)) {
            when {
                albumsState.isLoading -> {
                    Box(
                        modifier = Modifier.fillMaxSize(),
                        contentAlignment = Alignment.Center
                    ) {
                        CircularProgressIndicator(color = AppTheme.colors.secondaryDefaultLight)
                    }
                }

                albumsState.albums.isEmpty() -> {
                    Box(
                        modifier = Modifier.fillMaxSize(),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            text = stringResource(R.string.no_media_available),
                            color = AppTheme.colors.secondaryDefaultLight,
                            style = MaterialTheme.typography.bodyLarge
                        )
                    }
                }

                viewMode.value == AlbumViewMode.GRID -> {
                    LazyVerticalGrid(
                        state = gridState,
                        columns = GridCells.Fixed(3),
                        verticalArrangement = Arrangement.spacedBy(8.dp),
                        horizontalArrangement = Arrangement.spacedBy(8.dp),
                        modifier = Modifier.fillMaxSize()
                    ) {
                        items(albumsState.albums, key = { it.name }) { album ->
                            MediaAlbumCard(
                                album = album,
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .aspectRatio(1f)
                            ) {
                                viewModel.navToAlbumDetails(it)
                            }
                        }
                    }
                }

                else -> {
                    LazyColumn(
                        state = listState,
                        verticalArrangement = Arrangement.spacedBy(8.dp),
                        modifier = Modifier.fillMaxSize()
                    ) {
                        items(albumsState.albums, key = { it.name }) { album ->
                            MediaAlbumCard(
                                album = album,
                                modifier = Modifier.fillMaxWidth()
                            ) {
                                viewModel.navToAlbumDetails(it)
                            }
                        }
                    }
                }
            }
        }

        if (!albumsState.hasMediaPermission) {
            Column(modifier = Modifier.padding(8.dp)) {
                PermissionCard(
                    title = stringResource(R.string.media_access),
                    description = stringResource(R.string.allow_access_to_your_photos_and_videos_so_we_can_show_your_media_albums),
                    icon = rememberVectorPainter(Icons.Default.PermMedia)
                )
                Spacer(Modifier.height(10.dp))
                PrimaryButton(
                    text = "Agree",
                    onClick = { viewModel.checkMediaPermissionsIsGranted(context, launchPermissions = true) }
                )
            }
        }
    }
}
    @Preview
    @Composable
    fun ProfileContentPreview() {
        AppTheme {
            AlbumsDataContent(context = LocalContext.current, albumsState = AlbumsState(), hiltViewModel(), viewMode = remember { mutableStateOf(AlbumViewMode.GRID) })
        }
    }