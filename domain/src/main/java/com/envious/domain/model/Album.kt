package com.envious.domain.model

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class Album(
    val id: Int,
    val userId: Int,
    val title: String,
    var photos: List<Photo>
) : Parcelable
