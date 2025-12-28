package com.rvcn.seekhoanimeapplication.data.remote.model

import com.google.gson.annotations.SerializedName

data class AnimeResponse (
    @SerializedName("data")
    val data: List<AnimeDto>
)