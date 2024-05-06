package com.sergimarrahyarenas.bloodstats.api

import android.util.Log
import com.sergimarrahyarenas.bloodstats.api.Constants.BASE_ACCESS_TOKEN_URL
import com.sergimarrahyarenas.bloodstats.api.Constants.BASE_GAME_DATA_URL
import com.sergimarrahyarenas.bloodstats.api.Constants.BASE_PROFILE_URL
import com.sergimarrahyarenas.bloodstats.api.Constants.BEARER
import com.sergimarrahyarenas.bloodstats.api.Constants.CLIENT_ID
import com.sergimarrahyarenas.bloodstats.api.Constants.CLIENT_SECRET
import com.sergimarrahyarenas.bloodstats.api.RetrofitApiClient.RetrofitInstance.retrofit
import com.sergimarrahyarenas.bloodstats.models.characterdata.CharacterData
import com.sergimarrahyarenas.bloodstats.models.charactermedia.CharacterMedia
import com.sergimarrahyarenas.bloodstats.models.itemdata.ItemData
import com.sergimarrahyarenas.bloodstats.models.itemmedia.ItemMedia
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
        val response = retrofit(BASE_PROFILE_URL).getCharacterData(accessToken = "$BEARER $accessToken", characterName = name, realm = realm)
        Log.d("RESPONSE getCharacterData: ", "$response")
        if (response.isSuccessful) {
            response.body()
        } else {
            null
        }
    }.await()

    suspend fun getCharacterMedia(accessToken: String, name: String, realm: String?): CharacterMedia? = CoroutineScope(Dispatchers.IO).async {
        val response = retrofit(BASE_PROFILE_URL).getCharacterMedia(accessToken = "$BEARER $accessToken", characterName = name, realm = realm)
        Log.d("RESPONSE getCharacterMedia: ", "$response")
        if (response.isSuccessful) {
            response.body()
        } else {
            null
        }
    }.await()

    suspend fun getItemData(accessToken: String, name: String): ItemData? = CoroutineScope(Dispatchers.IO).async {
        val response = retrofit(BASE_GAME_DATA_URL).getItemData(accessToken = "$BEARER $accessToken", itemName = name)
        Log.d("RESPONSE getGearData: ", "$response")
        if (response.isSuccessful) {
            response.body()
        } else {
            null
        }
    }.await()

    suspend fun getItemMedia(accessToken: String, itemId: Int): ItemMedia? = CoroutineScope(Dispatchers.IO).async {
        val response = retrofit(BASE_GAME_DATA_URL).getItemMedia(accessToken = "$BEARER $accessToken", itemId = itemId)
        Log.d("RESPONSE getItemMedia: ", "$response")
        if (response.isSuccessful) {
            response.body()
        } else {
            null
        }
    }.await()
}