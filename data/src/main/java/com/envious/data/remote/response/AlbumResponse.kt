package com.envious.data.remote.response

import androidx.annotation.Keep
import com.envious.domain.model.Album
import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@Keep
@JsonClass(generateAdapter = true)
data class AlbumResponsItem(
    @Json(name = "id")
    val id: Int,
    @Json(name = "title")
    val title: String? = null,
    @Json(name = "userId")
    val userId: Int
) {
    fun toAlbum() : Album {
        return Album(
            id = id,
            userId = userId,
            title = title.orEmpty(),
            photos = emptyList()
        )
    }
}
