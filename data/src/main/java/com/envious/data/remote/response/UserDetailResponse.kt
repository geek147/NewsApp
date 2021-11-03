package com.envious.data.remote.response

import androidx.annotation.Keep
import com.envious.domain.model.UserDetail
import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@Keep
@JsonClass(generateAdapter = true)
data class UserDetailResponse(
    @Json(name = "address")
    val address: Address? = null,
    @Json(name = "company")
    val company: Company? = null,
    @Json(name = "email")
    val email: String? = null,
    @Json(name = "id")
    val id: Int,
    @Json(name = "name")
    val name: String? = null,
    @Json(name = "phone")
    val phone: String? = null,
    @Json(name = "username")
    val username: String? = null,
    @Json(name = "website")
    val website: String? = null
) {
    @Keep
    @JsonClass(generateAdapter = true)
    data class Address(
        @Json(name = "city")
        val city: String? = null,
        @Json(name = "geo")
        val geo: Geo? = null,
        @Json(name = "street")
        val street: String? = null,
        @Json(name = "suite")
        val suite: String? = null,
        @Json(name = "zipcode")
        val zipcode: String? = null
    ) {
        @Keep
        @JsonClass(generateAdapter = true)
        data class Geo(
            @Json(name = "lat")
            val lat: String? = null,
            @Json(name = "lng")
            val lng: String? = null
        )
    }

    @Keep
    @JsonClass(generateAdapter = true)
    data class Company(
        @Json(name = "bs")
        val bs: String? = null,
        @Json(name = "catchPhrase")
        val catchPhrase: String? = null,
        @Json(name = "name")
        val name: String? = null
    )

    fun toUserDetail(): UserDetail {
        return UserDetail(
            address = address?.street.orEmpty()+ " " + address?.city.orEmpty(),
            company = company?.name.orEmpty(),
            email = email.orEmpty(),
            name = name.orEmpty(),
            id = id
        )
    }
}
