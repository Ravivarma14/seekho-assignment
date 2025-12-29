package com.rvcn.seekhoanimeapplication.data.local.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "anime_detail")
data class AnimeDetailEntity(

    @PrimaryKey
    val id: Int,

    val title: String,
    val imageUrl: String?,
    val rating: Double?,
    val episodes: Int?,
    val synopsis: String?,
    val trailerThumbnailUrl: String?,
    val trailerYoutubeId: String?,

    val genres: String,
    val cast: String
)
