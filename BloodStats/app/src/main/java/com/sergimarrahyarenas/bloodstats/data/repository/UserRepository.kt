package com.sergimarrahyarenas.bloodstats.data.repository

import com.sergimarrahyarenas.bloodstats.data.database.dao.UserDao
import com.sergimarrahyarenas.bloodstats.data.database.entities.FavoriteEntity
import com.sergimarrahyarenas.bloodstats.data.database.entities.UserEntity
import com.sergimarrahyarenas.bloodstats.data.database.entities.UserFavoriteCrossRef
import com.sergimarrahyarenas.bloodstats.data.database.pojos.UserWithPreferences

class UserRepository(private val userDao: UserDao) {
    suspend fun insertUser(user: UserEntity) {
        userDao.insertUser(user)
    }

    suspend fun insertUserFavoriteCrossRef(user: UserFavoriteCrossRef) {
        userDao.insertUserFavoriteCrossRef(user)
    }

    suspend fun getUserByUUID(userUUID: String): UserEntity? {
        return userDao.getUserByUUID(userUUID)
    }

    suspend fun getUserByName(userName: String): UserEntity? {
        return userDao.getUserByName(userName)
    }

    suspend fun getUserByEmail(userEmail: String): UserEntity? {
        return userDao.getUserByEmail(userEmail)
    }

    suspend fun getUserFavorite(userUUID: String, characterName: String): FavoriteEntity? {
        return userDao.getUserFavorite(userUUID, characterName)
    }

    suspend fun getUserFavorites(userUUID: String): List<FavoriteEntity> {
        return userDao.getUserFavorites(userUUID)
    }

    suspend fun getUserWithPreferences(userUUID: String): UserWithPreferences {
        return userDao.getUserWithPreferences(userUUID)
    }

    suspend fun deleteUser(userUUID: String) {
        userDao.deleteUserByUUID(userUUID)
    }

    suspend fun deleteUserFavoriteCrossRef(crossRef: UserFavoriteCrossRef) {
        userDao.deleteUserFavoriteCrossRef(crossRef)
    }

}