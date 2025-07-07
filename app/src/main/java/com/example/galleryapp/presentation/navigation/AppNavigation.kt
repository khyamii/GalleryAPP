package com.example.galleryapp.presentation.navigation

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.lifecycle.SavedStateHandle
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.toRoute
import com.example.galleryapp.domain.media.model.Album
import com.example.galleryapp.presentation.feature.albumDetails.view.AlbumDetailsScreen
import com.example.galleryapp.presentation.feature.albums.view.AlbumsScreen
import com.example.galleryapp.presentation.navigation.Routes.AlbumsDetails
import com.example.galleryapp.presentation.navigation.navType.AlbumsDetailsNavType
import kotlinx.serialization.Serializable
import kotlin.reflect.typeOf

@Serializable
sealed class Routes {


    @Serializable
    data class AlbumsDetails(val albumData: Album) : Routes() {

        companion object {
            val typeMap = mapOf(typeOf<Album>() to AlbumsDetailsNavType.AlbumsDetailsType)

            fun from(savedStateHandle: SavedStateHandle) =
                savedStateHandle.toRoute<AlbumsDetails>(typeMap)
        }
    }

    @Serializable
    data object Albums : Routes()
}


@Composable
fun AppNavHost(
    modifier: Modifier = Modifier,
    navController: NavHostController,
) {
    NavHost(
        modifier = modifier,
        navController = navController,
        startDestination = Routes.Albums
    ) {

        composable<AlbumsDetails>(typeMap = AlbumsDetails.typeMap) { navBackStackEntry ->

            val album: AlbumsDetails = navBackStackEntry.toRoute()
            AlbumDetailsScreen(
                navController = navController, albumData = album.albumData
            )

        }

        composable<Routes.Albums> {
            AlbumsScreen(navController = navController)
        }

    }
}