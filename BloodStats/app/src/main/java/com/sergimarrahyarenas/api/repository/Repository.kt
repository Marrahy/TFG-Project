package com.sergimarrahyarenas.api.repository

import android.util.Log
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.sergimarrahyarenas.api.models.CharacterStatistics
import com.sergimarrahyarenas.api.network.NetworkManager
import okhttp3.OkHttpClient
import okhttp3.Request
import okio.IOException

class Repository(private val networkManager: NetworkManager) {
    private val okHttpClient = OkHttpClient()
    private val gson = Gson()
    suspend fun getCharacterStatistics(ACCESS_TOKEN: String, NAME: String, REALM: String?): CharacterStatistics? {
        return try {
            networkManager.getCharacterStatistics(ACCESS_TOKEN, NAME, REALM, gson)
        } catch (e: Exception) {
            null
        }
    }


}