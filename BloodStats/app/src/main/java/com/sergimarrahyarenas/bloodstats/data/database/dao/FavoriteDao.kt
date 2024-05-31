package com.sergimarrahyarenas.bloodstats.data.database.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Transaction
import com.sergimarrahyarenas.bloodstats.data.database.entities.FavoriteEntity
import com.sergimarrahyarenas.bloodstats.data.database.entities.UserFavoriteCrossRef

@Dao
interface FavoriteDao {

    @Insert
    suspend fun insertFavorite(favorite: FavoriteEntity)

    @Delete
    suspend fun deleteFavorite(favorite: FavoriteEntity)
}