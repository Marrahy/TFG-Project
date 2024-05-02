package com.sergimarrahyarenas.bloodstats.api

import com.sergimarrahyarenas.bloodstats.api.Constants.GRANT_TYPE
import com.sergimarrahyarenas.bloodstats.api.Constants.LOCALE_ES
import com.sergimarrahyarenas.bloodstats.api.Constants.NAMESPACE
import com.sergimarrahyarenas.bloodstats.api.models.characterdata.CharacterData
import retrofit2.Call
import retrofit2.Response
import retrofit2.http.Field
import retrofit2.http.FormUrlEncoded
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.POST
import retrofit2.http.Path
import retrofit2.http.Query

interface BlizzardApiService {

    @FormUrlEncoded
    @POST("token")
    suspend fun getAccessToken(
        @Header("Authorization") credentials: String,
        @Field("grant_type") grantType: String = GRANT_TYPE,
    ): Response<TokenResponse>

    @GET("character/{realm}/{name}")
    suspend fun getCharacterData(
        @Header("Authorization") accessToken: String,
        @Path("name") name: String,
        @Path("realm") realm: String?,
        @Query("namespace") namespace: String = NAMESPACE,
        @Query("locale") locale: String = LOCALE_ES
    ): Response<CharacterData>
}