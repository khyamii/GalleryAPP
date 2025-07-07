package com.example.galleryapp.presentation.navigation.navType

import android.net.Uri
import android.os.Bundle
import androidx.navigation.NavType
import com.example.galleryapp.domain.media.model.Album
import kotlinx.serialization.json.Json


object AlbumsDetailsNavType {
    val AlbumsDetailsType = object : NavType<Album>(isNullableAllowed = false) {
        override fun get(bundle: Bundle, key: String): Album? {
            return bundle.getString(key)?.let { Json.decodeFromString(Album.serializer(), it) }
        }

        override fun parseValue(value: String): Album {
            return Json.decodeFromString(Album.serializer(), Uri.decode(value))
        }

        override fun serializeAsValue(value: Album): String {
            return Uri.encode(Json.encodeToString(Album.serializer(), value))
        }

        override fun put(bundle: Bundle, key: String, value: Album) {
            bundle.putString(key, Json.encodeToString(Album.serializer(), value))
        }
    }
}