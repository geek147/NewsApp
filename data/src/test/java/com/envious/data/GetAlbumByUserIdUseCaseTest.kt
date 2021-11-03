package com.envious.data

import com.envious.data.usecase.GetAlbumByUserIdUseCase
import com.envious.data.usecase.GetCommentByPostIdUseCase
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
class GetAlbumByUserIdUseCaseTest {

    private val repository: PostRepository = mockk()
    private var getAlbumByUserId: GetAlbumByUserIdUseCase = mockk()

    @Before
    fun setUp() {
        getAlbumByUserId = GetAlbumByUserIdUseCase(repository)
    }

    @Test
    fun verify_getAlbumByUserId_call_getAlbumByUserIdRepository() {

        coEvery {
            repository.getAlbumByUserId(1)
        } returns Result.Success(data = emptyList())
        val params = GetAlbumByUserIdUseCase.Params(1)

        runBlockingTest {
            getAlbumByUserId(params)
        }

        coVerify {
            repository.getAlbumByUserId(1)
        }
    }
}
