package com.envious.data.repository

import android.util.Log
import com.envious.data.remote.PostApiService
import com.envious.domain.model.*
import com.envious.domain.repository.PostRepository
import com.envious.domain.util.Result
import javax.inject.Inject

class PostRepositoryImpl @Inject constructor(
    private val apiService: PostApiService
) : PostRepository {

    override suspend fun getPosts(): Result<List<Post>> {
        return try {
            val result = apiService.getPosts()
            return if (result.isSuccessful) {
                val remoteData = result.body()
                if (remoteData != null) {
                    // add newlogic for add new user detail
                    val newList = mutableListOf<Post>()
                    remoteData.forEach {
                        val post = it.toPost()
                        when (val resultDetail = getUserDetailById(it.userId)) {
                            is Result.Success -> {
                                post.userName = if (resultDetail.data.isEmpty()) "" else resultDetail.data.first().name
                                post.userCompanyName = if (resultDetail.data.isEmpty()) "" else resultDetail.data.first().company
                            }
                        }
                        newList.add(post)
                    }

                    Result.Success(newList.toList())
                } else {
                    Result.Success(emptyList())
                }
            } else {
                Result.Error(Exception("Invalid data/failure"))
            }
        } catch (e: Exception) {
            Log.e("ApiCalls", "Call error: ${e.localizedMessage}", e.cause)
            Result.Error(Exception(e.cause))
        }
    }

    override suspend fun getCommentByPostId(postId: Int): Result<List<Comment>> {
        return try {
            val result = apiService.getCommentByPostId(postId)
            return if (result.isSuccessful) {
                val remoteData = result.body()
                if (remoteData != null) {
                    Result.Success(remoteData.map { it.toComment() })
                } else {
                    Result.Success(emptyList())
                }
            } else {
                Result.Error(Exception("Invalid data/failure"))
            }
        } catch (e: Exception) {
            Log.e("ApiCalls", "Call error: ${e.localizedMessage}", e.cause)
            Result.Error(Exception(e.cause))
        }
    }

    override suspend fun getUserDetailById(userId: Int): Result<List<UserDetail>> {
        return try {
            val result = apiService.getUserDetailById(
                userId = userId
            )
            return if (result.isSuccessful) {
                val remoteData = result.body()
                if (remoteData != null) {
                    Result.Success(listOf(remoteData.toUserDetail()))
                } else {
                    Result.Success(emptyList())
                }
            } else {
                Result.Error(Exception("Invalid data/failure"))
            }
        } catch (e: Exception) {
            Log.e("ApiCalls", "Call error: ${e.localizedMessage}", e.cause)
            Result.Error(Exception(e.cause))
        }
    }

    override suspend fun getAlbumByUserId(userId: Int): Result<List<Album>> {
        return try {
            val result = apiService.getAlbumByUserId(userId)
            return if (result.isSuccessful) {
                val remoteData = result.body()
                if (remoteData != null) {
                    val newList = mutableListOf<Album>()
                    remoteData.forEach {
                        val album = it.toAlbum()
                        when (val resultDetail = getPhotosByAlbumId(it.id)) {
                            is Result.Success -> {
                                album.photos = if (resultDetail.data.isEmpty()) emptyList() else resultDetail.data
                            }
                        }
                        newList.add(album)
                    }

                    Result.Success(newList.toList())
                } else {
                    Result.Success(emptyList())
                }
            } else {
                Result.Error(Exception("Invalid data/failure"))
            }
        } catch (e: Exception) {
            Log.e("ApiCalls", "Call error: ${e.localizedMessage}", e.cause)
            Result.Error(Exception(e.cause))
        }
    }

    override suspend fun getPhotosByAlbumId(albumId: Int): Result<List<Photo>> {
        return try {
            val result = apiService.getPhotosByAlbumId(albumId)
            return if (result.isSuccessful) {
                val remoteData = result.body()
                if (remoteData != null) {
                    Result.Success(remoteData.map { it.toPhoto() })
                } else {
                    Result.Success(emptyList())
                }
            } else {
                Result.Error(Exception("Invalid data/failure"))
            }
        } catch (e: Exception) {
            Log.e("ApiCalls", "Call error: ${e.localizedMessage}", e.cause)
            Result.Error(Exception(e.cause))
        }
    }
}
