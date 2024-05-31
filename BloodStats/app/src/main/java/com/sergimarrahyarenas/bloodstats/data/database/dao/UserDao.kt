package com.sergimarrahyarenas.bloodstats.data.database.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Transaction
import com.sergimarrahyarenas.bloodstats.data.database.entities.FavoriteEntity
import com.sergimarrahyarenas.bloodstats.data.database.entities.UserEntity
import com.sergimarrahyarenas.bloodstats.data.database.entities.UserFavoriteCrossRef
import com.sergimarrahyarenas.bloodstats.data.database.pojos.UserWithPreferences

@Dao
interface UserDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertUser(user: UserEntity)

    @Insert
    suspend fun insertUserFavoriteCrossRef(userFavoriteCrossRef: UserFavoriteCrossRef)

    @Delete
    suspend fun deleteUserFavoriteCrossRef(crossRef: UserFavoriteCrossRef)

    @Query("DELETE FROM user WHERE userUUID = :userUUID")
    suspend fun deleteUserByUUID(userUUID: String)

    @Query("SELECT * FROM user WHERE userUUID = :userUUID")
    suspend fun getUserByUUID(userUUID: String): UserEntity?

    @Query("SELECT * FROM user WHERE user_name = :userName")
    suspend fun getUserByName(userName: String): UserEntity?

    @Query("SELECT * FROM user WHERE user_email = :userEmail")
    suspend fun getUserByEmail(userEmail: String): UserEntity?

    @Query("SELECT * FROM favorite WHERE userId = :userUUID AND characterName = :characterName")
    suspend fun getUserFavorite(userUUID: String, characterName: String): FavoriteEntity?

    @Transaction
    @Query("SELECT * FROM user WHERE userUUID = :userUUID")
    suspend fun getUserWithPreferences(userUUID: String): UserWithPreferences

    @Transaction
    @Query("""
        SELECT * FROM favorite f
        INNER JOIN UserFavoriteCrossRef ufcr ON f.favoriteUUID = ufcr.favoriteUUID
        WHERE ufcr.userUUID = :userUUID
    """)
    suspend fun getUserFavorites(userUUID: String): List<FavoriteEntity>

}