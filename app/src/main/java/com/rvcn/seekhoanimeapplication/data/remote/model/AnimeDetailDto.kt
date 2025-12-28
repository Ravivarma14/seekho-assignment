package com.rvcn.seekhoanimeapplication.data.remote.model

import com.google.gson.annotations.SerializedName

data class AnimeDetailDto(
    @SerializedName("mal_id")
    val id: Int,

    val title: String,

    val synopsis: String?,

    val episodes: Int?,

    val score: Double?,

    val images: ImageDto,

    val trailer: TrailerDto?,

    val genres: List<GenreDto>,

    val characters: List<CharacterWrapperDto>
)

data class TrailerDto(
    @SerializedName("youtube_id")
    val youtubeId: String?,

    val url: String?,

    @SerializedName("embed_url")
    val embedUrl: String?,

    val images: TrailerImagesDto?
)

data class TrailerImagesDto(
    @SerializedName("large_image_url")
    val largeImageUrl: String?
)

data class GenreDto(
    val name: String
)

data class CharacterWrapperDto(
    val character: CharacterDto
)

data class CharacterDto(
    val name: String,
    val images: ImageDto
)

