package com.sergimarrahyarenas.api.viewmodel

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.sergimarrahyarenas.api.models.characterstatistics.CharacterStatistics
import com.sergimarrahyarenas.api.TokenResponse
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.launch
import okhttp3.Credentials
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.RequestBody.Companion.toRequestBody
import org.json.JSONObject

class BlizzardViewModel: ViewModel() {

    private val baseProfileUrl = "https://eu.api.blizzard.com/profile"
    private val baseAccessTokenRequest = "https://oauth.battle.net/token"
    private val gson = Gson()

    private val okHttpClient = OkHttpClient()

    private val _isLoading = MutableLiveData<Boolean>()
    val isLoading: LiveData<Boolean> = _isLoading

    private val _accessToken = MutableLiveData<String>()
    val accessToken: LiveData<String> = _accessToken

    private val _characterStatistics = MutableLiveData<CharacterStatistics?>()
    val characterStatistics: LiveData<CharacterStatistics?> = _characterStatistics

    private val _entityName = MutableLiveData<String>()
    val entityName: LiveData<String> = _entityName

    private val _realm = MutableLiveData<String>()
    val realm: LiveData<String> = _realm


    init {
        viewModelScope.launch(Dispatchers.IO) {
            _accessToken.postValue(getAccessToken().accessToken)
        }
    }

    private suspend fun getAccessToken(): TokenResponse = CoroutineScope(Dispatchers.IO).async {
        val clientId = "286817623f474bcface004dc20313828"
        val clientSecret = "UCysV7ZsseanapD1lOfVg7FRTpCj2GXl"
        val credentials = Credentials.basic(clientId, clientSecret)
        val requestBody = "grant_type=client_credentials".toRequestBody("application/x-www-form-urlencoded".toMediaType())
        val request = Request.Builder()
            .url(baseAccessTokenRequest)
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

    fun loadCharacterStatistics() {
        viewModelScope.launch(Dispatchers.IO) {
            val characterStatistics = getCharacterStatistics()
            _characterStatistics.postValue(characterStatistics)
        }
    }

    private fun getCharacterStatistics(): CharacterStatistics? {
        val request = Request.Builder()
            .url("$baseProfileUrl/wow/character/${realm.value}/${entityName.value}/statistics?namespace=profile-eu&locale=en_US&access_token=${accessToken.value}")
            .header("Authorization", "Bearer ${accessToken.value}")
            .build()

        Log.d("Request body: ", "$request")

        return try {
            val response = okHttpClient.newCall(request).execute()
            Log.d("Response body: ", "$response")
            if (response.isSuccessful) {
                response.body?.string()?.let { parseCharacterStatistics(it) }
            } else {
                null
            }
        } catch (e: Exception) {
            null
        }
    }

    private fun parseCharacterStatistics(json: String): CharacterStatistics {
        val characterStatisticsType = object : TypeToken<CharacterStatistics>() {}.type
        return gson.fromJson(json, characterStatisticsType)
    }

    fun getEntityInformation(entityName: String) {

    }
}
