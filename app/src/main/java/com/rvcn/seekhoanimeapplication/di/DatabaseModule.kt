package com.rvcn.seekhoanimeapplication.di

import android.content.Context
import androidx.room.Room
import com.rvcn.seekhoanimeapplication.data.local.dao.AnimeDao
import com.rvcn.seekhoanimeapplication.data.local.dao.AnimeDetailDao
import com.rvcn.seekhoanimeapplication.data.local.db.AnimeDatabase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton


@Module
@InstallIn(SingletonComponent::class)
object DatabaseModule {

    @Provides
    @Singleton
    fun provideAnimeDatabse(@ApplicationContext context: Context): AnimeDatabase{

        return Room.databaseBuilder(context, AnimeDatabase::class.java,"anime_db").build()
    }

    @Provides
    @Singleton
    fun provideAnimeDao(animeDatabase: AnimeDatabase): AnimeDao{
        return animeDatabase.animeDao()
    }

    @Provides
    @Singleton
    fun provideAnimeDetailDao(animeDatabase: AnimeDatabase): AnimeDetailDao{
        return  animeDatabase.animeDetailDao()
    }
}