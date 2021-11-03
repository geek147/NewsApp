package com.envious.data

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
class GetCommentByPostIdUseCaseTest {

    private val repository: PostRepository = mockk()
    private var getCommentByPostId: GetCommentByPostIdUseCase = mockk()

    @Before
    fun setUp() {
        getCommentByPostId = GetCommentByPostIdUseCase(repository)
    }

    @Test
    fun verify_getCommentByPostId_call_getCommentByPostIdRepository() {

        coEvery {
            repository.getCommentByPostId(1)
        } returns Result.Success(data = emptyList())

        val params = GetCommentByPostIdUseCase.Params(1)
        runBlockingTest {
            getCommentByPostId(params)
        }

        coVerify {
            repository.getCommentByPostId(1)
        }
    }
}
