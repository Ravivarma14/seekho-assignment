package com.rvcn.seekhoanimeapplication.data.repository

import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.rvcn.seekhoanimeapplication.data.local.dao.AnimeDao
import com.rvcn.seekhoanimeapplication.data.local.dao.AnimeDetailDao
import com.rvcn.seekhoanimeapplication.data.local.entity.AnimeDetailEntity
import com.rvcn.seekhoanimeapplication.data.local.entity.AnimeEntity
import com.rvcn.seekhoanimeapplication.data.remote.JikanApiService
import com.rvcn.seekhoanimeapplication.data.remote.model.AnimeDetailDto
import com.rvcn.seekhoanimeapplication.domain.model.Anime
import com.rvcn.seekhoanimeapplication.domain.model.AnimeDetail
import com.rvcn.seekhoanimeapplication.domain.model.Cast
import com.rvcn.seekhoanimeapplication.domain.model.DataSource
import com.rvcn.seekhoanimeapplication.utils.YoutubeUtil
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

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

    suspend fun getAnimeDetail(id: Int): AnimeDetailResult {

        return try {
            val response = api.getAnimeDetail(id)
            if (!response.isSuccessful) throw Exception()

            val dto = response.body()!!.data

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

            val detail = mapDtoToDetail(dto,cast)

            animeDetailDao.insert(mapToEntity(detail))

            AnimeDetailResult(
                detail = detail,
                source = DataSource.NETWORK
            )

        } catch (e: Exception) {
            val cached = animeDetailDao.getDetailById(id)
                ?: throw Exception("No cached data")

            AnimeDetailResult(
                detail = mapEntityToDetail(cached),
                source = DataSource.CACHE
            )
        }
    }

    private fun mapDtoToDetail(
        dto: AnimeDetailDto,
        cast: List<Cast>
    ): AnimeDetail {

        val youtubeId = YoutubeUtil.extractVideoId(dto.trailer)

        return AnimeDetail(
            id = dto.id,
            title = dto.title,
            imageUrl = dto.images.jpg.imageUrl,
            rating = dto.score,
            episodes = dto.episodes,
            synopsis = dto.synopsis,
            trailerThumbnailUrl = dto.trailer?.images?.largeImageUrl,
            trailerYoutubeId = youtubeId,
            genres = dto.genres.map { it.name },
            cast = cast
        )
    }

    private fun mapToEntity(detail: AnimeDetail): AnimeDetailEntity {

        return AnimeDetailEntity(
            id = detail.id,
            title = detail.title,
            imageUrl = detail.imageUrl,
            rating = detail.rating,
            episodes = detail.episodes,
            synopsis = detail.synopsis,
            trailerThumbnailUrl = detail.trailerThumbnailUrl,
            trailerYoutubeId = detail.trailerYoutubeId,
            genres = detail.genres.joinToString(","),
            cast = Gson().toJson(detail.cast)
        )
    }

    private fun mapEntityToDetail(entity: AnimeDetailEntity): AnimeDetail {

        return AnimeDetail(
            id = entity.id,
            title = entity.title,
            imageUrl = entity.imageUrl,
            rating = entity.rating,
            episodes = entity.episodes,
            synopsis = entity.synopsis,
            trailerThumbnailUrl = entity.trailerThumbnailUrl,
            trailerYoutubeId = entity.trailerYoutubeId,
            genres = entity.genres.split(","),
            cast = Gson().fromJson(
                entity.cast,
                object : TypeToken<List<Cast>>() {}.type
            )
        )
    }

}

data class AnimeDetailResult(
    val detail: AnimeDetail,
    val source: DataSource
)