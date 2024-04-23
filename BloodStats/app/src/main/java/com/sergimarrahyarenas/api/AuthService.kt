package com.sergimarrahyarenas.api

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.RequestBody.Companion.toRequestBody
import org.json.JSONObject

class AuthService() {
    private val client = OkHttpClient()

    suspend fun getAccessToken(clientId: String, clientSecret: String): String {
        val json = JSONObject()
        json.put("client_id", clientId)
        json.put("client_secret", clientSecret)
        json.put("grant_type", "client_credentials")

        val requestBody = json.toString().toRequestBody("application/x-www-form-urlencoded".toMediaTypeOrNull())

        val request = Request.Builder()
            .url("https://eu.battle.net/oauth/token")
            .post(requestBody)
            .build()

        return withContext(Dispatchers.IO) {
            val response = client.newCall(request).execute()

            if (!response.isSuccessful) {
                throw RuntimeException("Failed to get access token")
            }

            val responseJson = response.body?.string()?.let { JSONObject(it) }

            responseJson!!.getString("access_token")
        }


    }
}