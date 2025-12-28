package com.rvcn.seekhoanimeapplication.data.local.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.rvcn.seekhoanimeapplication.data.local.entity.AnimeDetailEntity

@Dao
interface AnimeDetailDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(detail: AnimeDetailEntity)

    @Query("SELECT * FROM anime_detail WHERE id = :id")
    suspend fun getDetailById(id: Int): AnimeDetailEntity?
}
