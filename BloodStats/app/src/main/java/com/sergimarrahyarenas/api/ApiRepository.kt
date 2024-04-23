package com.sergimarrahyarenas.api

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class ApiRepository(private val apiService: ApiService) {
    suspend fun getGameData(game: String):GameDataResponse = withContext(Dispatchers.IO) {
        apiService.getGameData(game)
    }
}