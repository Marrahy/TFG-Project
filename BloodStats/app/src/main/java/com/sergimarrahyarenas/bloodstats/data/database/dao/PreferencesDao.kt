package com.sergimarrahyarenas.bloodstats.data.database.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update
import com.sergimarrahyarenas.bloodstats.data.database.entities.PreferencesEntity

@Dao
interface PreferencesDao {

    @Insert
    suspend fun insertPreferences(preferences: PreferencesEntity)

    @Update
    suspend fun updatePreferences(preferences: PreferencesEntity)

    @Delete
    suspend fun deletePreferences(preferences: PreferencesEntity)

    @Query("SELECT * FROM preferences WHERE userId = :userUUID")
    suspend fun getPreferencesByUserUUID(userUUID: String): PreferencesEntity

    @Query("UPDATE preferences SET theme = :newTheme WHERE userId = :userUUID")
    suspend fun updateThemeByUserUUID(userUUID: String, newTheme: String)

}