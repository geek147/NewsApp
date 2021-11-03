package com.envious.domain.repository

import com.envious.domain.model.Album
import com.envious.domain.model.Comment
import com.envious.domain.model.Photo
import com.envious.domain.model.Post
import com.envious.domain.model.UserDetail
import com.envious.domain.util.Result

interface PostRepository {

    suspend fun getPosts(): Result<List<Post>>

    suspend fun getCommentByPostId(
        postId: Int
    ): Result<List<Comment>>

    suspend fun getUserDetailById(
        userId: Int
    ): Result<List<UserDetail>>

    suspend fun getAlbumByUserId(
        userId: Int
    ): Result<List<Album>>

    suspend fun getPhotosByAlbumId(
        albumId: Int
    ): Result<List<Photo>>
}
