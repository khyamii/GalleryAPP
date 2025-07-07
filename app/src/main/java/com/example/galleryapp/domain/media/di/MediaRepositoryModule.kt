package com.example.galleryapp.domain.media.di

import com.example.galleryapp.data.repository.MediaRepositoryImpl
import com.example.galleryapp.domain.media.repository.MediaRepository
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton
@Module
@InstallIn(SingletonComponent::class)
object MediaRepositoryModule {
    @Provides
    @Singleton
    fun provideMediaRepository(): MediaRepository {
        return MediaRepositoryImpl()
    }
}