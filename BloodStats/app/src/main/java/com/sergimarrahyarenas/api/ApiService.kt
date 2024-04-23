package com.sergimarrahyarenas.api

import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class ApiService(private val authService: AuthService) {
    private val api: BlizzardApi

    init {
        val retrofit = Retrofit.Builder()
            .baseUrl("https://eu.api.blizzard.com/")
            .addConverterFactory(GsonConverterFactory.create())
            .build()

        api = retrofit.create(BlizzardApi::class.java)
    }

    suspend fun getGameData(game: String): GameDataResponse {
        val accessToken = authService.getAccessToken("286817623f474bcface004dc20313828", "UCysV7ZsseanapD1lOfVg7FRTpCj2GXl")
        return api.getGameData(game, accessToken).execute().body()!!
    }
}