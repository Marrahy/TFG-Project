package com.sergimarrahyarenas.bloodstats.database.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Transaction
import androidx.room.Update
import com.sergimarrahyarenas.bloodstats.database.entities.FavoriteEntity
import com.sergimarrahyarenas.bloodstats.database.entities.PreferencesEntity
import com.sergimarrahyarenas.bloodstats.database.entities.UserEntity
import com.sergimarrahyarenas.bloodstats.database.entities.UserFavoriteCrossRef
import com.sergimarrahyarenas.bloodstats.database.pojos.UserWithFavorites
import com.sergimarrahyarenas.bloodstats.database.pojos.UserWithPreferences

@Dao
interface UserDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertUser(user: UserEntity)

    @Insert
    suspend fun insertPreferences(preferences: PreferencesEntity)

    @Insert
    suspend fun insertFavorite(favorite: FavoriteEntity)

    @Insert
    suspend fun insertUserFavoriteCrossRef(userFavoriteCrossRef: UserFavoriteCrossRef)

    @Update
    suspend fun updatePreferences(preferences: PreferencesEntity)

    @Delete
    suspend fun deletePreferences(preferences: PreferencesEntity)

    @Delete
    suspend fun deleteFavorite(favorite: FavoriteEntity)

    @Delete
    suspend fun deleteUserFavoriteCrossRef(crossRef: UserFavoriteCrossRef)

    @Query("DELETE FROM user WHERE userUUID = :userId")
    suspend fun deleteUserById(userId: String)

    @Query("SELECT * FROM user WHERE user_name = :userName")
    suspend fun getUserByName(userName: String): UserEntity?

    @Query("SELECT * FROM user WHERE user_email = :userEmail")
    suspend fun getUserByEmail(userEmail: String): UserEntity?

    @Transaction
    @Query("SELECT * FROM user WHERE userUUID = :userId")
    suspend fun getUserWithPreferences(userId: String): UserWithPreferences?

    @Transaction
    @Query("SELECT * FROM user WHERE userUUID = :userId")
    suspend fun getUserWithFavorites(userId: String): UserWithFavorites?


}