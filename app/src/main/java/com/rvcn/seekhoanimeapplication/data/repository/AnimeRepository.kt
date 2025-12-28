package com.rvcn.seekhoanimeapplication.data.repository

import android.net.Uri
import android.util.Log
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.rvcn.seekhoanimeapplication.data.local.dao.AnimeDao
import com.rvcn.seekhoanimeapplication.data.local.dao.AnimeDetailDao
import com.rvcn.seekhoanimeapplication.data.local.entity.AnimeDetailEntity
import com.rvcn.seekhoanimeapplication.data.local.entity.AnimeEntity
import com.rvcn.seekhoanimeapplication.data.remote.JikanApiService
import com.rvcn.seekhoanimeapplication.domain.model.Anime
import com.rvcn.seekhoanimeapplication.domain.model.AnimeDetail
import com.rvcn.seekhoanimeapplication.domain.model.Cast
import com.rvcn.seekhoanimeapplication.utils.YoutubeUtil
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject
import androidx.core.net.toUri

class AnimeRepository @Inject constructor(
    private val api: JikanApiService,
    private val dao: AnimeDao,
    private val animeDetailDao: AnimeDetailDao

) {

    fun observeAnime(): Flow<List<Anime>> {
        return dao.getAllAnime().map { entities ->
            entities.map {
                Anime(
                    id = it.id,
                    title = it.title,
                    imageUrl = it.imageUrl,
                    episodes = it.episodes,
                    rating = it.rating,
                    synopsis = it.synopsis
                )
            }

        }
    }


    suspend fun refreshAnime(){

        val response = api.getTopAnime()

        if(response.isSuccessful){

            response.body()?.data?.map {
                AnimeEntity(
                    id = it.id,
                    title = it.title,
                    imageUrl = it.images.jpg.imageUrl,
                    episodes = it.episodes,
                    rating = it.score,
                    synopsis = it.synopsis
                )
            }?. let{ dao.insertAll(it)}
        }

    }

    suspend fun getAnimeDetail(id: Int): AnimeDetail {

        val detailResponse = api.getAnimeDetail(id)
        if (!detailResponse.isSuccessful) throw Exception()

        val dto = detailResponse.body()!!.data

        val charactersResponse = api.getAnimeCharacters(id)

        val cast = if (charactersResponse.isSuccessful) {
            charactersResponse.body()?.data
                ?.take(10)
                ?.map {
                    Cast(
                        name = it.character.name,
                        imageUrl = it.character.images.jpg.imageUrl
                    )
                } ?: emptyList()
        } else {
            emptyList()
        }

        val youtubeId = YoutubeUtil.extractVideoId(dto.trailer)
        Log.d("TAG", "getAnimeDetail: youtube link: $youtubeId")

        val detail = AnimeDetail(
            id = dto.id,
            title = dto.title,
            imageUrl = dto.images.jpg.imageUrl,
            rating = dto.score,
            episodes = dto.episodes,
            synopsis = dto.synopsis,
            trailerYoutubeId = youtubeId,
            trailerThumbnailUrl = dto.trailer?.images?.largeImageUrl,
            genres = dto.genres.map { it.name },
            cast = cast
        )

        // cache detail + cast
        animeDetailDao.insert(
            AnimeDetailEntity(
                id = detail.id,
                title = detail.title,
                imageUrl = detail.imageUrl,
                rating = detail.rating,
                episodes = detail.episodes,
                synopsis = detail.synopsis,
                trailerYoutubeId = detail.trailerYoutubeId,
                genres = detail.genres.joinToString(","),
                cast = Gson().toJson(detail.cast)
            )
        )

        return detail
    }



}