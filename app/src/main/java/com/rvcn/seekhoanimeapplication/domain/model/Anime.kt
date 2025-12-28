package com.rvcn.seekhoanimeapplication.domain.model

data class Anime(
    val id: Int,
    val title: String,
    val imageUrl: String?,
    val episodes: Int?,
    val rating: Double?,
    val synopsis: String?
)
