package com.envious.data.remote.response

import androidx.annotation.Keep
import com.envious.domain.model.Post
import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@Keep
@JsonClass(generateAdapter = true)
data class PostResponseItem(
    @Json(name = "body")
    val body: String? = null,
    @Json(name = "id")
    val id: Int,
    @Json(name = "title")
    val title: String? = null,
    @Json(name = "userId")
    val userId: Int
) {
    fun toPost(): Post {
        return Post(
            id = id,
            userName = "",
            userId = userId,
            body = body.orEmpty(),
            userCompanyName = "",
            title = title.orEmpty()
        )
    }
}
