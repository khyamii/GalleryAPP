package com.example.galleryapp.presentation.common


sealed class Result<T : Any>() {
    class Loading<T : Any> : Result<T>()
    data class Success<T : Any>(val data: T) : Result<T>()
    data class Error<T : Any>(val error: String) : Result<T>()
}