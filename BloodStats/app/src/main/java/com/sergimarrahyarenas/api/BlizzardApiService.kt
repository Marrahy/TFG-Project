package com.sergimarrahyarenas.api

import com.sergimarrahyarenas.api.models.bossdata.BossData
import com.sergimarrahyarenas.api.models.characterdata.CharacterData
import retrofit2.Response
import retrofit2.http.Field
import retrofit2.http.FormUrlEncoded
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.POST
import retrofit2.http.Path
import retrofit2.http.Query

interface BlizzardApiService {

    companion object {
        private val NAMESPACE = "profile-eu"
        private val LOCALE_ES = "es_ES"
    }

    @FormUrlEncoded
    @POST
    suspend fun getAccessToken(
        @Field("grant_type") grantType: String = "grant_type",
        @Header("Authorization") credentials: String
    ): Response<TokenResponse>

    @GET("character/{realm}/{name}")
    suspend fun getCharacterData(
        @Path("realm") realm: String?,
        @Path("name") name: String,
        @Query("namespace") namespace: String = NAMESPACE,
        @Query("locale") locale: String = LOCALE_ES,
        @Header("Authorization") accessToken: String
    ): Response<CharacterData>

    @GET("journal-encounter/{npcId}")
    suspend fun getBossData(
        @Path("npcId") name: String,
        @Path("namespace") namespace: String = NAMESPACE,
        @Path("locale") locale: String = LOCALE_ES,
        @Header("Authorization") accessToken: String
    ): Response<BossData>
}