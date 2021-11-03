package com.envious.data.remote

import com.envious.data.remote.response.* // ktlint-disable no-wildcard-imports
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.http.GET
import retrofit2.http.Path

interface PostApiService {

    @GET("posts")
    suspend fun getPosts(): Response<List<PostResponseItem>>

    @GET("posts/{postId}/comments")
    suspend fun getCommentByPostId(
        @Path("postId") postId: Int
    ): Response<List<CommentResponseItem>>

    @GET("users/{userId}")
    suspend fun getUserDetailById(
        @Path("userId") userId: Int
    ): Response<UserDetailResponse>

    @GET("users/{userId}/albums")
    suspend fun getAlbumByUserId(
        @Path("userId") userId: Int
    ): Response<List<AlbumResponsItem>>

    @GET("albums/{albumId}/photos")
    suspend fun getPhotosByAlbumId(
        @Path("albumId") albumId: Int
    ): Response<List<PhotoResponseItem>>

    companion object {
        operator fun invoke(retrofit: Retrofit): PostApiService = retrofit.create(PostApiService::class.java)
    }
}
