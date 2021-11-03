package com.envious.kumparan.util

import com.envious.domain.model.*

sealed class Intent {
    data class GetCommentByPostId(val postId: Int) : Intent()
    data class GetAlbumByUserId(val userId: Int) : Intent()
    data class GetDetailUser(val userId: Int) : Intent()
    object GetPost : Intent()
    data class GetPhotosByAlbumId(val albumId: Int) : Intent()
}

data class State(
    val showLoading: Boolean = false,
    val listPost: List<Post> = listOf(),
    val listAlbum: List<Album> = listOf(),
    val listPhoto: List<Photo> = listOf(),
    val listComment: List<Comment> = listOf(),
    val viewState: ViewState = ViewState.Idle,
    val detailUser: UserDetail? = null,
)

sealed class ViewState {
    object Idle : ViewState()
    object SuccessListPhoto : ViewState()
    object EmptyListPhoto : ViewState()
    object ErrorListPhoto : ViewState()
    object SuccessListComment : ViewState()
    object EmptyListComment : ViewState()
    object ErrorListComment : ViewState()
    object SuccessListAlbum : ViewState()
    object EmptyListAlbum : ViewState()
    object ErrorListAlbum : ViewState()
    object SuccessListPost : ViewState()
    object EmptyListPost : ViewState()
    object ErrorListPost : ViewState()
    object SuccessGetDetailUser : ViewState()
    object ErrorGetDetailUser : ViewState()
}
