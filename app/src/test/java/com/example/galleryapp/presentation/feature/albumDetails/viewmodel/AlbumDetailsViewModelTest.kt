package com.example.galleryapp.presentation.feature.albumDetails.viewmodel

import android.content.Context
import com.example.galleryapp.domain.media.usecase.GetAllMediaUseCase
import io.mockk.mockk
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.setMain
import android.net.Uri
import app.cash.turbine.test
import com.example.galleryapp.domain.media.model.MediaItem
import com.example.galleryapp.presentation.common.Result
import io.mockk.coEvery
import io.mockk.every
import junit.framework.TestCase.assertEquals
import junit.framework.TestCase.assertFalse
import junit.framework.TestCase.assertTrue
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.test.runTest
import org.junit.After
import org.junit.Before
import org.junit.Test

class AlbumDetailsViewModelTest {

    private val testDispatcher = StandardTestDispatcher()
    private lateinit var viewModel: AlbumDetailsViewModel
    private val getAllMediaUseCase: GetAllMediaUseCase = mockk()

    @OptIn(ExperimentalCoroutinesApi::class)
    @Before
    fun setup() {
        Dispatchers.setMain(testDispatcher)
        viewModel = AlbumDetailsViewModel(getAllMediaUseCase)
    }

    @OptIn(ExperimentalCoroutinesApi::class)
    @After
    fun tearDown() {
        Dispatchers.resetMain()
    }

    @Test
    fun `loadMediaForAlbum emits loading and success state for All Images`() = runTest {
        val context = mockk<Context>(relaxed = true)
        val mockUri = mockk<Uri>()
        every { mockUri.toString() } returns "uri1"

        val media = listOf(
            MediaItem(
                name = "image1",
                uri = mockUri,
                isVideo = false,
                bucketName = "DCIM",
                dateAdded = 1234567890L,
                mimeType = "image/jpeg"
            )
        )

        coEvery { getAllMediaUseCase(context) } returns flow {
            emit(Result.Loading())
            emit(Result.Success(media))
        }

        viewModel.loadMediaForAlbum(context, "All Images")

        viewModel.albumDetailsState.test {
            skipItems(1)
            val loading = awaitItem()
            assertTrue(loading.isLoading)

            val success = awaitItem()
            assertFalse(success.isLoading)
            assertEquals(1, success.allMedia.size)
            assertEquals("image1", success.allMedia.first().name)

            cancelAndConsumeRemainingEvents()
        }
    }

    @Test
    fun `loadMediaForAlbum emits error state`() = runTest {
        val context = mockk<Context>(relaxed = true)

        coEvery { getAllMediaUseCase(context) } returns flow {
            emit(Result.Loading())
            emit(Result.Error("Something went wrong"))
        }

        viewModel.loadMediaForAlbum(context, "DCIM")

        viewModel.albumDetailsState.test {
            skipItems(1)
            val loading = awaitItem()
            assertTrue(loading.isLoading)

            val errorState = awaitItem()
            assertFalse(errorState.isLoading)
            assertEquals("No media found  !", errorState.error)

            cancelAndConsumeRemainingEvents()
        }
    }

}