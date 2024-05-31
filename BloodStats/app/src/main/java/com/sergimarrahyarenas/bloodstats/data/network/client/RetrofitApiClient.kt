package com.sergimarrahyarenas.bloodstats.data.network.client

import android.util.Log
import com.sergimarrahyarenas.bloodstats.data.network.api.BlizzardApiService
import com.sergimarrahyarenas.bloodstats.utils.Constants.BASE_ACCESS_TOKEN_URL
import com.sergimarrahyarenas.bloodstats.utils.Constants.BASE_DATA_URL
import com.sergimarrahyarenas.bloodstats.utils.Constants.BASE_PROFILE_URL
import com.sergimarrahyarenas.bloodstats.utils.Constants.BEARER
import com.sergimarrahyarenas.bloodstats.utils.Constants.CLIENT_ID
import com.sergimarrahyarenas.bloodstats.utils.Constants.CLIENT_SECRET
import com.sergimarrahyarenas.bloodstats.data.network.client.RetrofitApiClient.RetrofitInstance.retrofit
import com.sergimarrahyarenas.bloodstats.model.accesstoken.TokenResponse
import com.sergimarrahyarenas.bloodstats.model.characetermythickeystone.CharacterMythicKeystone
import com.sergimarrahyarenas.bloodstats.model.characterencounters.CharacterEncounters
import com.sergimarrahyarenas.bloodstats.model.characterprofilesummary.CharacterProfileSummary
import com.sergimarrahyarenas.bloodstats.model.characterequipment.CharacterEquipment
import com.sergimarrahyarenas.bloodstats.model.characterguildroster.CharacterGuildRoster
import com.sergimarrahyarenas.bloodstats.model.charactermedia.CharacterMedia
import com.sergimarrahyarenas.bloodstats.model.characterspecialization.CharacterSpecialization
import com.sergimarrahyarenas.bloodstats.model.characterstatistics.CharacterStatistics
import com.sergimarrahyarenas.bloodstats.model.itemdata.ItemData
import com.sergimarrahyarenas.bloodstats.model.itemmedia.ItemMedia
import com.sergimarrahyarenas.bloodstats.model.realm.RealmInfo
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

    suspend fun getCharacterProfileSummary(accessToken: String, characterName: String, realmSlug: String): CharacterProfileSummary? = CoroutineScope(Dispatchers.IO).async {
        val response = retrofit(BASE_PROFILE_URL).getCharacterProfileSummary(accessToken = "$BEARER $accessToken", characterName = characterName.lowercase(), realmSlug = realmSlug)
        if (response.isSuccessful && response.code() != 404) response.body() else null
    }.await()

    suspend fun getCharacterStatisticsSummary(accessToken: String, characterName: String, realmSlug: String): CharacterStatistics? = CoroutineScope(Dispatchers.IO).async {
        val response = retrofit(BASE_PROFILE_URL).getCharacterStatisticsSummary(accessToken = "$BEARER $accessToken", characterName = characterName.lowercase(), realmSlug = realmSlug)
        if (response.isSuccessful) response.body() else null
    }.await()

    suspend fun getCharacterSpecialization(accessToken: String, characterName: String, realmSlug: String): CharacterSpecialization? = CoroutineScope(Dispatchers.IO).async {
        val response = retrofit(BASE_PROFILE_URL).getCharacterSpecialization(accessToken = "$BEARER $accessToken", characterName = characterName.lowercase(), realmSlug = realmSlug)
        if (response.isSuccessful) response.body() else null
    }.await()

    suspend fun getCharacterEquipment(accessToken: String, characterName: String, realmSlug: String): CharacterEquipment? = CoroutineScope(Dispatchers.IO).async {
        val response = retrofit(BASE_PROFILE_URL).getCharacterEquipmentSummary(accessToken = "$BEARER $accessToken", characterName = characterName.lowercase(), realmSlug = realmSlug)
        if (response.isSuccessful) response.body() else null
    }.await()

    suspend fun getCharacterGuildRoster(accessToken: String, characterName: String, realmSlug: String): CharacterGuildRoster? = CoroutineScope(Dispatchers.IO).async {
        val response = retrofit(BASE_DATA_URL).getCharacterGuildRoster(accessToken = "$BEARER $accessToken", characterName = characterName.lowercase(), realmSlug = realmSlug)
        if (response.isSuccessful) response.body() else null
    }.await()

    suspend fun getCharacterEncounters(accessToken: String, characterName: String, realmSlug: String): CharacterEncounters? = CoroutineScope(Dispatchers.IO).async {
        val response = retrofit(BASE_PROFILE_URL).getCharacterEncounters(accessToken = "$BEARER $accessToken", characterName = characterName.lowercase(), realmSlug = realmSlug)
        if (response.isSuccessful) response.body() else null
    }.await()

    suspend fun getCharacterMythicKeystone(accessToken: String, characterName: String, realmSlug: String): CharacterMythicKeystone? = CoroutineScope(Dispatchers.IO).async {
        val response = retrofit(BASE_PROFILE_URL).getCharacterMythicKeystone(accessToken = "$BEARER $accessToken", characterName = characterName.lowercase(), realmSlug = realmSlug)
        if (response.isSuccessful) response.body() else null
    }.await()

    suspend fun getCharacterMedia(accessToken: String, characterName: String, realmSlug: String?): CharacterMedia? = CoroutineScope(Dispatchers.IO).async {
        val response = retrofit(BASE_PROFILE_URL).getCharacterMedia(accessToken = "$BEARER $accessToken", characterName = characterName.lowercase(), realm = realmSlug)
        if (response.isSuccessful) response.body() else null
    }.await()

    suspend fun getItemDataById(accessToken: String, itemId: Int): ItemData? = CoroutineScope(Dispatchers.IO).async {
        val response = retrofit(BASE_DATA_URL).getItemDataById(accessToken = "$BEARER $accessToken", itemId = itemId)
        Log.d("itemid", "$itemId")
        if (response.isSuccessful) response.body() else null
    }.await()

    suspend fun getItemMedia(accessToken: String, itemId: Int): ItemMedia? = CoroutineScope(Dispatchers.IO).async {
        val response = retrofit(BASE_DATA_URL).getItemMedia(accessToken = "$BEARER $accessToken", itemId = itemId)
        Log.d("itemid", "$itemId")
        Log.d("itemid", "$response")
        if (response.isSuccessful) response.body() else null
    }.await()

    suspend fun getListOfEURealms(accessToken: String): List<RealmInfo>? = CoroutineScope(Dispatchers.IO).async {
        val response = retrofit(BASE_DATA_URL).getListOfEURealms(accessToken = "$BEARER $accessToken")
        if (response.isSuccessful) response.body()?.realms else null
    }.await()
}