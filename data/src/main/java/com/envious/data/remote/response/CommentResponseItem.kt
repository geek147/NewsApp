package com.envious.data.remote.response

import androidx.annotation.Keep
import com.envious.domain.model.Comment
import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@Keep
@JsonClass(generateAdapter = true)
data class CommentResponseItem(
    @Json(name = "body")
    val body: String? = null,
    @Json(name = "email")
    val email: String? = null,
    @Json(name = "id")
    val id: Int,
    @Json(name = "name")
    val name: String? = null,
    @Json(name = "postId")
    val postId: Int
) {
    fun toComment(): Comment {
        return Comment(
            author = name.orEmpty(),
            id = id,
            postId = postId,
            body = body.orEmpty()
        )
    }
}

