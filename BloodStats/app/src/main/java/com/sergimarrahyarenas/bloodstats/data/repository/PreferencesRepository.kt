package com.sergimarrahyarenas.bloodstats.data.repository

import com.sergimarrahyarenas.bloodstats.data.database.dao.PreferencesDao
import com.sergimarrahyarenas.bloodstats.data.database.entities.PreferencesEntity

class PreferencesRepository(private val preferencesDao: PreferencesDao) {

    suspend fun insertPreferences(preferences: PreferencesEntity) {
        preferencesDao.insertPreferences(preferences)
    }

    suspend fun updatePreferences(preferences: PreferencesEntity) {
        preferencesDao.updatePreferences(preferences)
    }

    suspend fun deletePreferences(preferences: PreferencesEntity) {
        preferencesDao.deletePreferences(preferences)
    }

    suspend fun getPreferencesByUserUUID(userUUID: String): PreferencesEntity {
        return preferencesDao.getPreferencesByUserUUID(userUUID)
    }

    suspend fun updateThemeByUserUUID(userUUID: String, newTheme: String) {
        preferencesDao.updateThemeByUserUUID(userUUID, newTheme)
    }

}