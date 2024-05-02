package com.sergimarrahyarenas.bloodstats.api

import android.util.Log
import com.sergimarrahyarenas.bloodstats.api.Constants.BASE_ACCESS_TOKEN_URL
import com.sergimarrahyarenas.bloodstats.api.Constants.BASE_PROFILE_URL
import com.sergimarrahyarenas.bloodstats.api.Constants.CLIENT_ID
import com.sergimarrahyarenas.bloodstats.api.Constants.CLIENT_SECRET
import com.sergimarrahyarenas.bloodstats.api.RetrofitApiClient.RetrofitInstance.retrofit
import com.sergimarrahyarenas.bloodstats.api.models.characterdata.CharacterData
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import okhttp3.Credentials
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class RetrofitApiClient {

object RetrofitInstance {
    fun retrofit(BASE_URL: String): BlizzardApiService =
        Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(BlizzardApiService::class.java)
}

    suspend fun getAccessTokenApiService(): TokenResponse? = CoroutineScope(Dispatchers.IO).async {
        val credentials = Credentials.basic(CLIENT_ID, CLIENT_SECRET)

        val response = retrofit(BASE_ACCESS_TOKEN_URL).getAccessToken(credentials)

        if (response.isSuccessful) {
            response.body()
        } else {
            null
        }
    }.await()

    suspend fun getCharacterData(accessToken: String, name: String, realm: String?): CharacterData? = CoroutineScope(Dispatchers.IO).async {
        val response = retrofit(BASE_PROFILE_URL).getCharacterData(accessToken = "Bearer $accessToken", name = name, realm = realm)

        if (response.isSuccessful) {
            response.body()
        } else {
            null
        }
    }.await()
}