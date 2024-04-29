package com.sergimarrahyarenas.api.network

import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.sergimarrahyarenas.api.TokenResponse
import com.sergimarrahyarenas.api.models.CharacterStatistics
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.withContext
import okhttp3.Credentials
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.RequestBody.Companion.toRequestBody
import org.json.JSONObject

class NetworkManager(private val okHttpClient: OkHttpClient) {

    val BASE_ACCESS_TOKEN_REQUEST = "https://oauth.battle.net/token"
    val BASE_CHARACTER_DATA_EU_REQUEST = "https://eu.api.blizzard.com/profile"
    val NAME_SPACE_EU = "profile-eu"
    val LOCALE_ES = "es_ES"

    //Esta funcion se encarga de realizar la peticion al servidor y recibir el token de acceso
    suspend fun getAccessToken(clientId: String, clientSecret: String): TokenResponse = CoroutineScope(Dispatchers.IO).async {
        val credentials = Credentials.basic(clientId, clientSecret)
        val requestBody = "grant_type=client_credentials"
            .toRequestBody("application/x-www-form-urlencoded".toMediaType())
        val request = Request.Builder()
            .url(BASE_ACCESS_TOKEN_REQUEST)
            .post(requestBody)
            .header("Authorization", credentials)
            .build()

        val response = okHttpClient.newCall(request).execute()

        if (response.isSuccessful) {
            val responseBody = response.body!!.string()
            val json = JSONObject(responseBody)
            return@async TokenResponse(
                accessToken = json.getString("access_token")
            )
        } else {
            throw Exception("Failed to get access token")
        }
    }.await()

    //Esta funcion se encarga de realizar la peticion al servidor y recibir los atributos de un personaje con el nombre pasado como parametro
    suspend fun getCharacterStatistics(ACCESS_TOKEN: String, NAME: String, REALM: String?, gson: Gson): CharacterStatistics? {
        return withContext(Dispatchers.IO) {
            val request = Request.Builder()
                .url("${BASE_CHARACTER_DATA_EU_REQUEST}/wow/character/$REALM/$NAME/statistics?namespace=${NAME_SPACE_EU}&locale=${LOCALE_ES}")
                .header("Authorization", "Bearer $ACCESS_TOKEN")
                .build()

            val response = okHttpClient.newCall(request).execute()

            if (response.isSuccessful) {
                val responseBody = response.body?.string()
                parseCharacterStatistics(responseBody, gson)
            } else {
                null
            }
        }
    }

    //Deserializa el JSON recibido a un objeto especifico, CharacterStatistics
    private fun parseCharacterStatistics(json: String?, gson: Gson): CharacterStatistics {
        val characterStatisticsType = object : TypeToken<CharacterStatistics>() {}.type
        return gson.fromJson(json, characterStatisticsType)
    }
}