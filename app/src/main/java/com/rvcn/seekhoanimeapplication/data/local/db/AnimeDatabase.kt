package com.rvcn.seekhoanimeapplication.data.local.db

import androidx.room.Database
import androidx.room.RoomDatabase
import com.rvcn.seekhoanimeapplication.data.local.dao.AnimeDao
import com.rvcn.seekhoanimeapplication.data.local.dao.AnimeDetailDao
import com.rvcn.seekhoanimeapplication.data.local.entity.AnimeDetailEntity
import com.rvcn.seekhoanimeapplication.data.local.entity.AnimeEntity


@Database(entities = [AnimeEntity::class,  AnimeDetailEntity::class], exportSchema = false, version = 1)
abstract class AnimeDatabase : RoomDatabase() {

    abstract fun animeDao(): AnimeDao
    abstract fun animeDetailDao(): AnimeDetailDao
}