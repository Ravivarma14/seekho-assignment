package com.rvcn.seekhoanimeapplication.data.remote

import com.rvcn.seekhoanimeapplication.data.remote.model.AnimeCharactersResponse
import com.rvcn.seekhoanimeapplication.data.remote.model.AnimeDetailResponse
import com.rvcn.seekhoanimeapplication.data.remote.model.AnimeResponse
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Path

interface JikanApiService {

    @GET("top/anime")
    suspend fun getTopAnime(): Response<AnimeResponse>

    @GET("anime/{id}/full")
    suspend fun getAnimeDetail(
        @Path("id") id: Int
    ): Response<AnimeDetailResponse>

    @GET("anime/{id}/characters")
    suspend fun getAnimeCharacters(
        @Path("id") id: Int
    ): Response<AnimeCharactersResponse>

}