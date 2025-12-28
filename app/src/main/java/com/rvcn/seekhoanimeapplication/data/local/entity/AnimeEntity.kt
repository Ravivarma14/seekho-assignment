package com.rvcn.seekhoanimeapplication.data.local.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity("anime")
data class AnimeEntity (

    @PrimaryKey
    val id: Int,
    val title: String,
    val imageUrl: String?,
    val episodes: Int?,
    val rating: Double?,
    val synopsis: String?
)