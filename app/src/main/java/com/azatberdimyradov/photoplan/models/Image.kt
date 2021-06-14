package com.azatberdimyradov.photoplan.models

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class Image(
    val id: String? = null,
    val imageUrl: String? = null
): Parcelable
