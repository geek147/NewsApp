package com.envious.data

import com.envious.data.usecase.GetDetailUserUseCase
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
class GetUserDetailUseCaseTest {

    private val repository: PostRepository = mockk()
    private var getUserDetail: GetDetailUserUseCase = mockk()

    @Before
    fun setUp() {
        getUserDetail = GetDetailUserUseCase(repository)
    }

    @Test
    fun verify_getAlbumByUserId_call_getAlbumByUserIdRepository() {

        coEvery {
            repository.getUserDetailById(1)
        } returns Result.Success(data = emptyList())
        val params = GetDetailUserUseCase.Params(1)

        runBlockingTest {
            getUserDetail(params)
        }

        coVerify {
            repository.getUserDetailById(1)
        }
    }
}
