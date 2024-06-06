package com.sergimarrahyarenas.bloodstats.data.database.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import com.sergimarrahyarenas.bloodstats.data.database.entities.FavoriteEntity

@Dao
interface FavoriteDao {

    @Insert
    suspend fun insertFavorite(favorite: FavoriteEntity)

    @Delete
    suspend fun deleteFavorite(favorite: FavoriteEntity)
}