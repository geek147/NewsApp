package com.envious.data

import com.envious.data.usecase.GetPostUseCase
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
class GetPostUseCaseTest {

    private val repository: PostRepository = mockk()
    private var getPostUseCase: GetPostUseCase = mockk()

    @Before
    fun setUp() {
        getPostUseCase = GetPostUseCase(repository)
    }

    @Test
    fun verify_getPost_call_getPostRepository() {

        coEvery {
            repository.getPosts()
        } returns Result.Success(data = emptyList())

        runBlockingTest {
            getPostUseCase()
        }

        coVerify {
            repository.getPosts()
        }
    }
}
