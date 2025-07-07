package com.example.galleryapp.presentation.feature.albums.viewmodel

import android.content.Context
import android.net.Uri
import app.cash.turbine.test
import com.example.galleryapp.domain.media.model.MediaItem
import com.example.galleryapp.domain.media.usecase.GetAllMediaUseCase
import com.example.galleryapp.presentation.common.Result
import io.mockk.coEvery
import io.mockk.every
import io.mockk.mockk
import junit.framework.TestCase.assertEquals
import junit.framework.TestCase.assertFalse
import junit.framework.TestCase.assertTrue
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import org.junit.After
import org.junit.Before
import org.junit.Test

class AlbumsViewModelTest {
    private val getAllMediaUseCase: GetAllMediaUseCase = mockk()
    private lateinit var viewModel: AlbumsViewModel
    private val testDispatcher = StandardTestDispatcher()

    @OptIn(ExperimentalCoroutinesApi::class)
    @Before
    fun setup() {
        Dispatchers.setMain(testDispatcher)
        viewModel = AlbumsViewModel(getAllMediaUseCase)
    }

    @OptIn(ExperimentalCoroutinesApi::class)
    @After
    fun tearDown() {
        Dispatchers.resetMain()
    }

    @Test
    fun `getAllMedia loading state`() = runTest {
        val context = mockk<Context>(relaxed = true)
        coEvery { getAllMediaUseCase(context) } returns flow {
            emit(Result.Loading())
            delay(100)
        }

        viewModel.albumsDataState.test {
            viewModel.getAllMedia(context)
            skipItems(1)
            val loading = awaitItem()
            assertTrue(loading.isLoading)
            cancelAndConsumeRemainingEvents()
        }
    }

    @Test
    fun `getAllMedia success state empty media`() = runTest {
        val context = mockk<Context>(relaxed = true)

        coEvery { getAllMediaUseCase(context) } returns flow {
            emit(Result.Loading())
            emit(Result.Success(emptyList()))
        }

        viewModel.getAllMedia(context)

        viewModel.albumsDataState.test {
            skipItems(1)
            val loading = awaitItem()
            assertTrue(loading.isLoading)

            val success = awaitItem()
            assertFalse(success.isLoading)
            assertTrue(success.albums.isEmpty())

            cancelAndConsumeRemainingEvents()
        }
    }

    @Test
    fun `getAllMedia success state only images`() = runTest {
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

        viewModel.getAllMedia(context)

        viewModel.albumsDataState.test {
            skipItems(1)
            val loading = awaitItem()
            assertTrue(loading.isLoading)

            val result = awaitItem()
            assertFalse(result.isLoading)
            assertEquals(2, result.albums.size)
            assertEquals("All Images", result.albums[0].name)
            cancelAndConsumeRemainingEvents()
        }
    }

@Test
fun `getAllMedia success state only videos`() = runTest {
    val context = mockk<Context>(relaxed = true)
    val mockUri = mockk<Uri>()
    every { mockUri.toString() } returns "uri1"

    val media = listOf(
        MediaItem(
            name = "video1",
            uri = mockUri,
            isVideo = true,
            bucketName = "Movies",
            dateAdded = 1234567890L,
            mimeType = "video/mp4"
        )
    )

    coEvery { getAllMediaUseCase(context) } returns flow {
        emit(Result.Loading())
        emit(Result.Success(media))
    }

    viewModel.getAllMedia(context)

    viewModel.albumsDataState.test {
        skipItems(1)

        val loading = awaitItem()
        assertTrue(loading.isLoading)

        val result = awaitItem()
        assertFalse(result.isLoading)
        assertEquals(2, result.albums.size)

        assertEquals("All Videos", result.albums[0].name)
        assertEquals("Movies", result.albums[1].name)
        assertTrue(result.albums.all { it.isVideo })

        cancelAndConsumeRemainingEvents()
    }
}

    @Test
    fun `getAllMedia error state`() = runTest {
        val context = mockk<Context>(relaxed = true)

        coEvery { getAllMediaUseCase(context) } returns flow {
            emit(Result.Loading())
            emit(Result.Error("Something went wrong"))
        }

        viewModel.getAllMedia(context)

        viewModel.albumsDataState.test {
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