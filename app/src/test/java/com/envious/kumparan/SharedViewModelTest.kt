package com.envious.kumparan

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.lifecycle.Observer
import com.envious.data.dispatchers.CoroutineDispatchers
import com.envious.data.usecase.* // ktlint-disable no-wildcard-imports
import com.envious.domain.model.*
import com.envious.domain.util.Result
import com.envious.kumparan.util.Intent
import com.envious.kumparan.util.State
import com.envious.kumparan.util.ViewState
import com.envious.kumparan.viewmodel.SharedViewModel
import io.mockk.coEvery
import io.mockk.every
import io.mockk.mockk
import io.mockk.slot
import junit.framework.Assert.assertEquals
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.TestCoroutineDispatcher
import kotlinx.coroutines.test.setMain
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import java.lang.Exception

@ExperimentalCoroutinesApi
class SharedViewModelTest {

    @get:Rule
    var instantTaskExecutorRule = InstantTaskExecutorRule()

    private var getAlbumByUserIdUseCase = mockk<GetAlbumByUserIdUseCase>()
    private var getDetailUserUseCase = mockk<GetDetailUserUseCase>()
    private var getCommentByPostIdUseCase = mockk<GetCommentByPostIdUseCase>()
    private var getPhotosByAlbumIdUseCase = mockk<GetPhotosByAlbumIdUseCase>()
    private var getPostUseCase = mockk<GetPostUseCase>()
    private var ioDispatcher = mockk<CoroutineDispatchers>()

    private val observedStateList = mutableListOf<State>()
    private val observerState = mockk<Observer<State>>()
    private val slotState = slot<State>()

    private val testDispatcher = TestCoroutineDispatcher()

    private val viewModel = SharedViewModel(
        getDetailUserUseCase,
        getAlbumByUserIdUseCase,
        getCommentByPostIdUseCase,
        getPhotosByAlbumIdUseCase,
        getPostUseCase,
        ioDispatchers = ioDispatcher
    )

    val userDetail = UserDetail(
        address = "Depok",
        company = "Condet",
        email = "asdf@gmailcom",
        name = "test1",
        id = 1
    )

    val post = Post(
        id = 12,
        userName = "test2",
        title = "nangka",
        userId = 2,
        userCompanyName = "baja",
        body = "test aja"
    )

    val comment = Comment(
        id = 1,
        postId = 1,
        author = "teta",
        body = "test lagi"
    )

    val album = Album(
        id = 14,
        userId = 16,
        title = "satu",
        photos = emptyList()
    )

    val photo = Photo(
        id = 12,
        albumId = 16,
        thumbnailUrl = "",
        url = "",
        title = ""
    )

    @Before
    fun setup() {
        Dispatchers.setMain(testDispatcher)

        viewModel.state.observeForever(observerState)

        every {
            observerState.onChanged(capture(slotState))
        } answers {
            observedStateList.add(slotState.captured)
        }
    }

    @After
    fun tearDown() {
        observedStateList.clear()

        viewModel.state.removeObserver(observerState)
        testDispatcher.cleanupTestCoroutines()
    }

    @Test
    fun `onGetDetailUser success should set view state to Success  `() {

        coEvery {
            ioDispatcher.io
        } returns testDispatcher

        coEvery {
            getDetailUserUseCase(any())
        } returns Result.Success(listOf(userDetail))

        viewModel.onIntentReceived(
            Intent.GetDetailUser(1)
        )

        assertEquals(observedStateList.last().showLoading, false)
        assertEquals(observedStateList.last().viewState, ViewState.SuccessGetDetailUser)
        assertEquals(observedStateList.last().detailUser?.id, 1)
    }

    @Test
    fun `onGetDetailUser Error should set view state to ErrorState  `() {

        coEvery {
            ioDispatcher.io
        } returns testDispatcher

        coEvery {
            getDetailUserUseCase(any())
        } returns Result.Error(exception = Exception())

        viewModel.onIntentReceived(
            Intent.GetDetailUser(1)
        )

        assertEquals(observedStateList.last().showLoading, false)
        assertEquals(observedStateList.last().viewState, ViewState.ErrorGetDetailUser)
    }

    @Test
    fun `onGetPost success should set view state to Success  `() {

        coEvery {
            ioDispatcher.io
        } returns testDispatcher

        coEvery {
            getPostUseCase()
        } returns Result.Success(listOf(post))

        viewModel.onIntentReceived(
            Intent.GetPost
        )

        assertEquals(observedStateList.last().showLoading, false)
        assertEquals(observedStateList.last().viewState, ViewState.SuccessListPost)
        assertEquals(observedStateList.last().listPost[0].id, 12)
    }

    @Test
    fun `onGetPost success but empty should set view state to Empty State  `() {

        coEvery {
            ioDispatcher.io
        } returns testDispatcher

        coEvery {
            getPostUseCase()
        } returns Result.Success(emptyList())

        viewModel.onIntentReceived(
            Intent.GetPost
        )

        assertEquals(observedStateList.last().showLoading, false)
        assertEquals(observedStateList.last().viewState, ViewState.EmptyListPost)
    }

    @Test
    fun `onGetPost Error should set view state to ErrorState  `() {

        coEvery {
            ioDispatcher.io
        } returns testDispatcher

        coEvery {
            getPostUseCase()
        } returns Result.Error(exception = Exception())

        viewModel.onIntentReceived(
            Intent.GetPost
        )

        assertEquals(observedStateList.last().showLoading, false)
        assertEquals(observedStateList.last().viewState, ViewState.ErrorListPost)
    }

    @Test
    fun `onGetCommentByPostId success should set view state to Success  `() {

        coEvery {
            ioDispatcher.io
        } returns testDispatcher

        coEvery {
            getCommentByPostIdUseCase(any())
        } returns Result.Success(listOf(comment))

        viewModel.onIntentReceived(
            Intent.GetCommentByPostId(1)
        )

        assertEquals(observedStateList.last().showLoading, false)
        assertEquals(observedStateList.last().viewState, ViewState.SuccessListComment)
        assertEquals(observedStateList.last().listComment[0].id, 1)
    }

    @Test
    fun `onGetCommentByPostId success but empty should set view state to Empty State  `() {

        coEvery {
            ioDispatcher.io
        } returns testDispatcher

        coEvery {
            getCommentByPostIdUseCase(any())
        } returns Result.Success(emptyList())

        viewModel.onIntentReceived(
            Intent.GetCommentByPostId(1)
        )

        assertEquals(observedStateList.last().showLoading, false)
        assertEquals(observedStateList.last().viewState, ViewState.EmptyListComment)
    }

    @Test
    fun `onGetCommentByPostId Error should set view state to ErrorState  `() {

        coEvery {
            ioDispatcher.io
        } returns testDispatcher

        coEvery {
            getPostUseCase()
        } returns Result.Error(exception = Exception())

        viewModel.onIntentReceived(
            Intent.GetPost
        )

        assertEquals(observedStateList.last().showLoading, false)
        assertEquals(observedStateList.last().viewState, ViewState.ErrorListComment)
    }

    @Test
    fun `onGetPhotosByAlbumId success should set view state to Success  `() {

        coEvery {
            ioDispatcher.io
        } returns testDispatcher

        coEvery {
            getPhotosByAlbumIdUseCase(any())
        } returns Result.Success(listOf(photo))

        viewModel.onIntentReceived(
            Intent.GetPhotosByAlbumId(1)
        )

        assertEquals(observedStateList.last().showLoading, false)
        assertEquals(observedStateList.last().viewState, ViewState.SuccessListPhoto)
        assertEquals(observedStateList.last().listPhoto[0].id, 12)
    }

    @Test
    fun `onGetPhotosByAlbumId success but empty should set view state to Empty State  `() {

        coEvery {
            ioDispatcher.io
        } returns testDispatcher

        coEvery {
            getPhotosByAlbumIdUseCase(any())
        } returns Result.Success(emptyList())

        viewModel.onIntentReceived(
            Intent.GetPhotosByAlbumId(1)
        )

        assertEquals(observedStateList.last().showLoading, false)
        assertEquals(observedStateList.last().viewState, ViewState.EmptyListPhoto)
    }

    @Test
    fun `onGetPhotosByAlbumId Error should set view state to ErrorState  `() {

        coEvery {
            ioDispatcher.io
        } returns testDispatcher

        coEvery {
            getPostUseCase()
        } returns Result.Error(exception = Exception())

        viewModel.onIntentReceived(
            Intent.GetPost
        )

        assertEquals(observedStateList.last().showLoading, false)
        assertEquals(observedStateList.last().viewState, ViewState.ErrorListPhoto)
    }

    @Test
    fun `onGetAlbumByUserId success should set view state to Success  `() {

        coEvery {
            ioDispatcher.io
        } returns testDispatcher

        coEvery {
            getAlbumByUserIdUseCase(any())
        } returns Result.Success(listOf(album))

        viewModel.onIntentReceived(
            Intent.GetAlbumByUserId(1)
        )

        assertEquals(observedStateList.last().showLoading, false)
        assertEquals(observedStateList.last().viewState, ViewState.SuccessListAlbum)
        assertEquals(observedStateList.last().listPhoto[0].id, 14)
    }

    @Test
    fun `onGetAlbumByUserId success but empty should set view state to Empty State  `() {

        coEvery {
            ioDispatcher.io
        } returns testDispatcher

        coEvery {
            getPhotosByAlbumIdUseCase(any())
        } returns Result.Success(emptyList())

        viewModel.onIntentReceived(
            Intent.GetPhotosByAlbumId(1)
        )

        assertEquals(observedStateList.last().showLoading, false)
        assertEquals(observedStateList.last().viewState, ViewState.EmptyListAlbum)
    }

    @Test
    fun `onGetAlbumByUserId Error should set view state to ErrorState  `() {

        coEvery {
            ioDispatcher.io
        } returns testDispatcher

        coEvery {
            getPostUseCase()
        } returns Result.Error(exception = Exception())

        viewModel.onIntentReceived(
            Intent.GetPost
        )

        assertEquals(observedStateList.last().showLoading, false)
        assertEquals(observedStateList.last().viewState, ViewState.ErrorListAlbum)
    }
}
