package com.sergimarrahyarenas.bloodstats.api.blizzardmanagement

import android.util.Log
import com.sergimarrahyarenas.bloodstats.api.blizzardmanagement.Constants.BASE_ACCESS_TOKEN_URL
import com.sergimarrahyarenas.bloodstats.api.blizzardmanagement.Constants.BASE_DATA_URL
import com.sergimarrahyarenas.bloodstats.api.blizzardmanagement.Constants.BASE_PROFILE_URL
import com.sergimarrahyarenas.bloodstats.api.blizzardmanagement.Constants.BEARER
import com.sergimarrahyarenas.bloodstats.api.blizzardmanagement.Constants.CLIENT_ID
import com.sergimarrahyarenas.bloodstats.api.blizzardmanagement.Constants.CLIENT_SECRET
import com.sergimarrahyarenas.bloodstats.api.blizzardmanagement.RetrofitApiClient.RetrofitInstance.retrofit
import com.sergimarrahyarenas.bloodstats.models.accesstoken.TokenResponse
import com.sergimarrahyarenas.bloodstats.models.characermythickeystoneprofile.CharacterMythicKeystoneProfile
import com.sergimarrahyarenas.bloodstats.models.characterprofilesummary.CharacterProfileSummary
import com.sergimarrahyarenas.bloodstats.models.characterequipment.CharacterEquipment
import com.sergimarrahyarenas.bloodstats.models.characterguildroster.CharacterGuildRoster
import com.sergimarrahyarenas.bloodstats.models.charactermedia.CharacterMedia
import com.sergimarrahyarenas.bloodstats.models.realm.Realm
import com.sergimarrahyarenas.bloodstats.models.characterspecialization.CharacterSpecialization
import com.sergimarrahyarenas.bloodstats.models.characterstatistics.CharacterStatistics
import com.sergimarrahyarenas.bloodstats.models.itemdata.ItemData
import com.sergimarrahyarenas.bloodstats.models.itemmedia.ItemMedia
import com.sergimarrahyarenas.bloodstats.models.realm.RealmInfo
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import okhttp3.Credentials
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class RetrofitApiClient {

object RetrofitInstance {
    fun retrofit(baseUrl: String): BlizzardApiService =
        Retrofit.Builder()
            .baseUrl(baseUrl)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(BlizzardApiService::class.java)
}

    suspend fun getAccessTokenApiService(): TokenResponse? = CoroutineScope(Dispatchers.IO).async {
        val credentials = Credentials.basic(CLIENT_ID, CLIENT_SECRET)
        val response = retrofit(BASE_ACCESS_TOKEN_URL).getAccessToken(credentials)
        if (response.isSuccessful) response.body() else null
    }.await()

    suspend fun getCharacterProfileSummary(accessToken: String, name: String, realm: String): CharacterProfileSummary? = CoroutineScope(Dispatchers.IO).async {
        val response = retrofit(BASE_PROFILE_URL).getCharacterProfileSummary(accessToken = "$BEARER $accessToken", characterName = name.lowercase(), realmSlug = realm)
        Log.d("RESPONSE getCharacterData: ", "$response")
        if (response.isSuccessful) response.body() else null
    }.await()

    suspend fun getCharacterStatisticsSummary(accessToken: String, name: String, realm: String): CharacterStatistics? = CoroutineScope(Dispatchers.IO).async {
        val response = retrofit(BASE_PROFILE_URL).getCharacterStatisticsSummary(accessToken = "$BEARER $accessToken", characterName = name.lowercase(), realmSlug = realm)
        Log.d("RESPONSE getCharacterStatisticsSummary: ", "$response")
        if (response.isSuccessful) response.body() else null
    }.await()

    suspend fun getCharacterSpecialization(accessToken: String, name: String, realm: String): CharacterSpecialization? = CoroutineScope(Dispatchers.IO).async {
        val response = retrofit(BASE_PROFILE_URL).getCharacterSpecialization(accessToken = "$BEARER $accessToken", characterName = name.lowercase(), realmSlug = realm)
        Log.d("RESPONSE getCharacterSpecialization: ", "$response")
        if (response.isSuccessful) response.body() else null
    }.await()

    suspend fun getCharacterEquipment(accessToken: String, name: String, realm: String): CharacterEquipment? = CoroutineScope(Dispatchers.IO).async {
        val response = retrofit(BASE_PROFILE_URL).getCharacterEquipmentSummary(accessToken = "$BEARER $accessToken", characterName = name.lowercase(), realmSlug = realm)
        Log.d("RESPONSE getCharacterEquipment: ", "$response")
        if (response.isSuccessful) response.body() else null
    }.await()

    suspend fun getCharacterGuildRoster(accessToken: String, name: String, realm: String): CharacterGuildRoster? = CoroutineScope(Dispatchers.IO).async {
        val response = retrofit(BASE_DATA_URL).getCharacterGuildRoster(accessToken = "$BEARER $accessToken", characterName = name, realmSlug = realm)
        Log.d("RESPONSE getCharacterGuildRoster: ", "$response")
        if (response.isSuccessful) response.body() else null
    }.await()

    suspend fun getCharacterMythicKeystoneProfile(accessToken: String, name: String, realm: String): CharacterMythicKeystoneProfile? = CoroutineScope(Dispatchers.IO).async {
        val response = retrofit(BASE_PROFILE_URL).getCharacterMythicKeystoneProfile(accessToken = "$BEARER $accessToken", characterName = name.lowercase(), realmSlug = realm)
        Log.d("RESPONSE getCharacterMythicKeystoneProfile: ", "$response")
        if (response.isSuccessful) response.body() else null
    }.await()

    suspend fun getCharacterMedia(accessToken: String, name: String, realm: String?): CharacterMedia? = CoroutineScope(Dispatchers.IO).async {
        val response = retrofit(BASE_PROFILE_URL).getCharacterMedia(accessToken = "$BEARER $accessToken", characterName = name.lowercase(), realm = realm)
        Log.d("RESPONSE getCharacterMedia: ", "$response")
        if (response.isSuccessful) response.body() else null
    }.await()

    suspend fun getItemDataById(accessToken: String, itemId: Int): ItemData? = CoroutineScope(Dispatchers.IO).async {
        val response = retrofit(BASE_DATA_URL).getItemDataById(accessToken = "$BEARER $accessToken", itemId = itemId)
        Log.d("RESPONSE getItemDataById: ", "$response")
        if (response.isSuccessful) response.body() else null
    }.await()

    suspend fun getItemData(accessToken: String, itemName: String): ItemData? = CoroutineScope(Dispatchers.IO).async {
        val response = retrofit(BASE_DATA_URL).getItemData(accessToken = "$BEARER $accessToken", itemName = itemName)
        Log.d("RESPONSE getItemData: ", "$response")
        if (response.isSuccessful) response.body() else null
    }.await()

    suspend fun getItemMedia(accessToken: String, itemId: Int): ItemMedia? = CoroutineScope(Dispatchers.IO).async {
        val response = retrofit(BASE_DATA_URL).getItemMedia(accessToken = "$BEARER $accessToken", itemId = itemId)
        Log.d("RESPONSE getItemMedia: ", "$response")
        if (response.isSuccessful) response.body() else null
    }.await()

    suspend fun getListOfEURealms(accessToken: String): List<RealmInfo>? = CoroutineScope(Dispatchers.IO).async {
        val response = retrofit(BASE_DATA_URL).getListOfEURealms(accessToken = "$BEARER $accessToken")
        Log.d("RESPONSE getListOfEURealms: ", "$response")
        if (response.isSuccessful) response.body()?.realms else null
    }.await()
}