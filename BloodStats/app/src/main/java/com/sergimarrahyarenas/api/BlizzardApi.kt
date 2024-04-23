package com.sergimarrahyarenas.api

import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.Query

interface BlizzardApi {
    @GET("data/wow/game-data")
    suspend fun getGameData(
        @Query("namespace") game: String,
        @Header("Authorization") accessToken: String
    ): Call<GameDataResponse>
}