package com.envious.domain.model

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class Comment(
    val body: String,
    val author: String,
    val id: Int,
    val postId: Int
) : Parcelable