package com.envious.kumparan.viewmodel

import androidx.lifecycle.viewModelScope
import com.envious.data.dispatchers.CoroutineDispatchers
import com.envious.data.usecase.*
import com.envious.domain.util.Result
import com.envious.kumparan.base.BaseViewModel
import com.envious.kumparan.util.Intent
import com.envious.kumparan.util.State
import com.envious.kumparan.util.ViewState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject

@HiltViewModel
class SharedViewModel @Inject constructor(
    private val getDetailUserUseCase: GetDetailUserUseCase,
    private val getAlbumByUserIdUseCase: GetAlbumByUserIdUseCase,
    private val getCommentByPostIdUseCase: GetCommentByPostIdUseCase,
    private val getPhotosByAlbumIdUseCase: GetPhotosByAlbumIdUseCase,
    private val getPostUseCase: GetPostUseCase,
    private val ioDispatchers: CoroutineDispatchers
) : BaseViewModel<Intent, State>(State()) {

    override fun onIntentReceived(intent: Intent) {
        when (intent) {
            is Intent.GetAlbumByUserId -> getAlbumbyUserId(intent.userId)
            is Intent.GetCommentByPostId -> getCommentByPostId(intent.postId)
            is Intent.GetDetailUser -> getDetailUser(intent.userId)
            is Intent.GetPhotosByAlbumId -> getPhotosByAlbumId(intent.albumId)
            Intent.GetPost -> getPost()
        }
    }

    private fun getPhotosByAlbumId(albumId: Int) {
        setState {
            copy(
                showLoading = true
            )
        }
        val param = GetPhotosByAlbumIdUseCase.Params(albumId)
        viewModelScope.launch {
            when (
                val result = withContext(ioDispatchers.io) {
                    getPhotosByAlbumIdUseCase(param)
                }
            ) {
                is Result.Success -> {
                    if (result.data.isEmpty()) {
                        setState {
                            copy(
                                viewState = ViewState.EmptyListPhoto,
                                listPhoto = emptyList(),
                                showLoading = false
                            )
                        }
                    } else {
                        setState {
                            copy(
                                viewState = ViewState.SuccessListPhoto,
                                listPhoto = result.data,
                                showLoading = false
                            )
                        }
                    }
                }
                is Result.Error -> {
                    setState {
                        copy(
                            viewState = ViewState.ErrorListPhoto,
                            listPhoto = emptyList(),
                            showLoading = false,
                        )
                    }
                }
            }
        }
    }

    private fun getCommentByPostId(postId: Int) {
        setState {
            copy(
                showLoading = true
            )
        }
        val param = GetCommentByPostIdUseCase.Params(postId)

        viewModelScope.launch {
            when (
                val result = withContext(ioDispatchers.io) {
                    getCommentByPostIdUseCase(param)
                }
            ) {
                is Result.Success -> {
                    if (result.data.isEmpty()) {
                        setState {
                            copy(
                                viewState = ViewState.EmptyListComment,
                                listComment = emptyList(),
                                showLoading = false
                            )
                        }
                    } else {
                        setState {
                            copy(
                                viewState = ViewState.SuccessListComment,
                                listComment = result.data,
                                showLoading = false
                            )
                        }
                    }
                }
                is Result.Error -> {
                    setState {
                        copy(
                            viewState = ViewState.ErrorListComment,
                            listComment = emptyList(),
                            showLoading = false,
                        )
                    }
                }
            }
        }
    }

    private fun getAlbumbyUserId(userId: Int) {
        setState {
            copy(
                showLoading = true
            )
        }

        val param = GetAlbumByUserIdUseCase.Params(userId)

        viewModelScope.launch {
            when (
                val result = withContext(ioDispatchers.io) {
                    getAlbumByUserIdUseCase(param)
                }
            ) {
                is Result.Success -> {
                    if (result.data.isEmpty()) {
                        setState {
                            copy(
                                viewState = ViewState.ErrorListAlbum,
                                listAlbum = emptyList(),
                                showLoading = false
                            )
                        }
                    } else {
                        setState {
                            copy(
                                viewState = ViewState.SuccessListAlbum,
                                listAlbum = result.data,
                                showLoading = false
                            )
                        }
                    }
                }
                is Result.Error -> {
                    setState {
                        copy(
                            viewState = ViewState.ErrorListAlbum,
                            listPost = emptyList(),
                            showLoading = false,
                        )
                    }
                }
            }
        }
    }

    private fun getDetailUser(userId: Int) {
        setState {
            copy(
                showLoading = true
            )
        }

        val params = GetDetailUserUseCase.Params(userId = userId)
        viewModelScope.launch {
            when (
                val result = withContext(ioDispatchers.io) {
                    getDetailUserUseCase(params)
                }
            ) {
                is Result.Success -> {
                    setState {
                        copy(
                            detailUser = if (result.data.isNotEmpty()) result.data.first() else null,
                            showLoading = false,
                            viewState = ViewState.SuccessGetDetailUser
                        )
                    }
                }
                is Result.Error -> {
                    setState {
                        copy(
                            showLoading = false,
                            viewState = ViewState.ErrorGetDetailUser
                        )
                    }
                }
            }
        }
    }

    private fun getPost() {
        setState {
            copy(
                showLoading = true
            )
        }
        viewModelScope.launch {
            when (
                val result = withContext(ioDispatchers.io) {
                    getPostUseCase()
                }
            ) {
                is Result.Success -> {
                    if (result.data.isEmpty()) {
                        setState {
                            copy(
                                viewState = ViewState.EmptyListPost,
                                listPost = emptyList(),
                                showLoading = false
                            )
                        }
                    } else {
                        setState {
                            copy(
                                viewState = ViewState.SuccessListPost,
                                listPost = result.data,
                                showLoading = false
                            )
                        }
                    }
                }
                is Result.Error -> {
                    setState {
                        copy(
                            viewState = ViewState.ErrorListPost,
                            listPost = emptyList(),
                            showLoading = false,
                        )
                    }
                }
            }
        }
    }
}
