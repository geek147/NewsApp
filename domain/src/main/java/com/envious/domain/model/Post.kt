package com.envious.domain.model

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class Post(
    val title: String,
    val body: String,
    var userName: String,
    var userCompanyName: String,
    val userId: Int,
    val id: Int
) : Parcelable
