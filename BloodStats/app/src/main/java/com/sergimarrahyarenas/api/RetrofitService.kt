package com.sergimarrahyarenas.api

import com.google.gson.Gson
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object RetrofitService {
    private val gson = Gson()

    val accessTokenRetrofit: Retrofit = Retrofit.Builder()
        .baseUrl("https://oauth.battle.net/token")
        .addConverterFactory(GsonConverterFactory.create())
        .client(OkHttpClient())
        .build()

    val blizzardApiProfileRequestRetrofit: Retrofit = Retrofit.Builder()
        .baseUrl("https://eu.api.blizzard.com/profile/wow/")
        .addConverterFactory(GsonConverterFactory.create(gson))
        .client(OkHttpClient())
        .build()

    val blizzardApiGameDataRequestRetrofit: Retrofit = Retrofit.Builder()
        .baseUrl("https://eu.api.blizzard.com/data/wow/")
        .addConverterFactory(GsonConverterFactory.create(gson))
        .client(OkHttpClient())
        .build()
}