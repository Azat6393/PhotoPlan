package com.azatberdimyradov.photoplan.models

data class Location(
    val id: String? = null,
    val locationName: String? = null,
    val imageList: List<Image>? = null
)
