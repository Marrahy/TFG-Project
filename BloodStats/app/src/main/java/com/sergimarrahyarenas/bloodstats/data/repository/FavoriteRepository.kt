package com.sergimarrahyarenas.bloodstats.data.repository

import com.sergimarrahyarenas.bloodstats.data.database.dao.FavoriteDao
import com.sergimarrahyarenas.bloodstats.data.database.entities.FavoriteEntity

class FavoriteRepository(private val favoriteDao: FavoriteDao) {

    suspend fun insertFavorite(favorite: FavoriteEntity) {
        favoriteDao.insertFavorite(favorite)
    }

    suspend fun deleteFavorite(favorite: FavoriteEntity) {
        favoriteDao.deleteFavorite(favorite)
    }
}