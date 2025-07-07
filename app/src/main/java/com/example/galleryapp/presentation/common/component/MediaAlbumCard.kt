package com.example.galleryapp.presentation.common.component
import android.util.Log
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import coil.request.ImageRequest
import coil.request.videoFrameMillis
import com.example.galleryapp.R
import com.example.galleryapp.domain.media.model.Album


@Composable
fun MediaAlbumCard(
    album: Album,
    modifier: Modifier = Modifier,
    onClick: (Album) -> Unit = {}
) {
    val context = LocalContext.current

    val request = ImageRequest.Builder(context)
        .data(album.coverUri)
        .crossfade(true)
        .size(200)
        .let { builder ->
            if (album.isVideo) {
                builder.videoFrameMillis(1000)
            }
            builder.build()
        }

    Box(
        modifier = modifier
            .clip(RoundedCornerShape(5.dp))
            .clickable { onClick(album) }
    ) {
        AsyncImage(
            model = request,
            contentDescription = album.name,
            contentScale = ContentScale.Crop,
            modifier = Modifier.fillMaxSize()
        )

        if (album.isVideo) {
            Icon(
                imageVector = Icons.Filled.PlayArrow,
                contentDescription = "Play",
                tint = Color.LightGray,
                modifier = Modifier
                    .align(Alignment.Center)
                    .size(36.dp)
            )
        }

        Box(
            modifier = Modifier
                .align(Alignment.BottomStart)
                .fillMaxWidth()
                .background(
                    Brush.verticalGradient(
                        colors = listOf(Color.Transparent, Color.Black.copy(alpha = 0.6f))
                    )
                )
                .padding(5.dp)
        ) {
            Column {
                Text(
                    text = album.name,
                    color = Color.White,
                    fontSize = 12.sp,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )
                Text(
                    text = "${album.mediaCount} items",
                    color = Color.LightGray,
                    fontSize = 10.sp
                )
            }
        }
    }
}