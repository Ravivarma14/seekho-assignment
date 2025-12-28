package com.rvcn.seekhoanimeapplication.data.remote.model

import com.google.gson.annotations.SerializedName

data class AnimeDto(
    @SerializedName("mal_id")
    val id: Int,
    val title: String,
    val episodes: Int?,
    val score: Double?,
    val synopsis: String?,

    @SerializedName("images")
    val images: ImageDto
)

data class ImageDto(
    @SerializedName("jpg")
    val jpg: JpgDto
)

data class JpgDto(
    @SerializedName("image_url")
    val imageUrl: String?
)
