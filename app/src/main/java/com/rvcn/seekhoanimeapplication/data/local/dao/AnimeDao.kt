package com.rvcn.seekhoanimeapplication.data.local.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.rvcn.seekhoanimeapplication.data.local.entity.AnimeEntity
import kotlinx.coroutines.flow.Flow


@Dao
interface AnimeDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAll (animeList: List<AnimeEntity>)

    @Query("Select * from anime")
    fun getAllAnime() : Flow<List<AnimeEntity>>

    @Query("Delete from anime")
    suspend fun clearAll()

}