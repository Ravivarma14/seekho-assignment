package com.rvcn.seekhoanimeapplication.domain.model

data class AnimeDetail(
    val id: Int,
    val title: String,
    val imageUrl: String?,
    val rating: Double?,
    val episodes: Int?,
    val synopsis: String?,
    val trailerYoutubeId: String?,
    val trailerThumbnailUrl: String?,
    val genres: List<String>,
    val cast: List<Cast>
)

data class Cast(
    val name: String,
    val imageUrl: String?
)
