package com.example.galleryapp.presentation.common.util

import android.content.Context
import android.content.pm.PackageManager
import androidx.core.content.ContextCompat

class GeneralUtility {
    companion object {

        fun checkPermissionIsGranted(context: Context, permission: String): Boolean {
            return ContextCompat.checkSelfPermission(
                context,
                permission
            ) == PackageManager.PERMISSION_GRANTED
        }
    }
}