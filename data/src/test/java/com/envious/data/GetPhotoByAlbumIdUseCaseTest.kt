package com.envious.data

import com.envious.data.usecase.GetAlbumByUserIdUseCase
import com.envious.data.usecase.GetPhotosByAlbumIdUseCase
import com.envious.domain.repository.PostRepository
import com.envious.domain.util.Result
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.mockk
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runBlockingTest
import org.junit.Before
import org.junit.Test

@ExperimentalCoroutinesApi
class GetPhotoByAlbumIdUseCaseTest {

    private val repository: PostRepository = mockk()
    private var getPhotoByAlbumId: GetPhotosByAlbumIdUseCase = mockk()

    @Before
    fun setUp() {
        getPhotoByAlbumId = GetPhotosByAlbumIdUseCase(repository)
    }

    @Test
    fun verify_getAlbumByUserId_call_getAlbumByUserIdRepository() {

        coEvery {
            repository.getPhotosByAlbumId(1)
        } returns Result.Success(data = emptyList())
        val params = GetPhotosByAlbumIdUseCase.Params(1)

        runBlockingTest {
            getPhotoByAlbumId(params)
        }

        coVerify {
            repository.getPhotosByAlbumId(1)
        }
    }
}
