package com.example.galleryapp.domain.media.di

import com.example.galleryapp.domain.media.repository.MediaRepository
import com.example.galleryapp.domain.media.usecase.GetAllMediaUseCase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object GetAllMediaUseCasesModule {

    @Singleton
    @Provides
    fun provideAllMediaUseCase(
        mediaRepository: MediaRepository
    ): GetAllMediaUseCase {
        return GetAllMediaUseCase(mediaRepository)
    }
}