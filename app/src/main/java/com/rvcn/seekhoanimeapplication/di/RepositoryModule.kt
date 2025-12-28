package com.rvcn.seekhoanimeapplication.di

import com.rvcn.seekhoanimeapplication.data.local.dao.AnimeDao
import com.rvcn.seekhoanimeapplication.data.local.dao.AnimeDetailDao
import com.rvcn.seekhoanimeapplication.data.remote.JikanApiService
import com.rvcn.seekhoanimeapplication.data.repository.AnimeRepository
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton


@Module
@InstallIn(SingletonComponent::class)
object RepositoryModule {

    @Provides
    @Singleton
    fun provideAnimeRepository(
        api: JikanApiService,
        dao: AnimeDao,
        animeDetailDao: AnimeDetailDao
    ): AnimeRepository {

        return AnimeRepository(api,dao,animeDetailDao)
    }
}