package com.envious.data.remote.response

import androidx.annotation.Keep
import com.envious.domain.model.Photo
import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@Keep
@JsonClass(generateAdapter = true)
data class PhotoResponseItem(
    @Json(name = "albumId")
    val albumId: Int,
    @Json(name = "id")
    val id: Int,
    @Json(name = "thumbnailUrl")
    val thumbnailUrl: String? = null,
    @Json(name = "title")
    val title: String? = null,
    @Json(name = "url")
    val url: String? = null
) {
    fun toPhoto(): Photo {
        return Photo(
            albumId = albumId,
            id = id,
            thumbnailUrl = thumbnailUrl.orEmpty(),
            title = title.orEmpty(),
            url = url.orEmpty()
        )
    }
}
