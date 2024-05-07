package com.sergimarrahyarenas.bloodstats.api

import com.sergimarrahyarenas.bloodstats.api.Constants.CHARACTER_MEDIA
import com.sergimarrahyarenas.bloodstats.api.Constants.GRANT_TYPE
import com.sergimarrahyarenas.bloodstats.api.Constants.ITEM_MEDIA
import com.sergimarrahyarenas.bloodstats.api.Constants.LOCALE_ES
import com.sergimarrahyarenas.bloodstats.api.Constants.NAMESPACE
import com.sergimarrahyarenas.bloodstats.api.Constants.STATIC_NAMESPACE
import com.sergimarrahyarenas.bloodstats.models.characterdata.CharacterData
import com.sergimarrahyarenas.bloodstats.models.charactermedia.CharacterMedia
import com.sergimarrahyarenas.bloodstats.models.itemdata.ItemData
import com.sergimarrahyarenas.bloodstats.models.itemmedia.ItemMedia
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
        @Path("name") characterName: String,
        @Path("realm") realm: String?,
        @Query("namespace") namespace: String = NAMESPACE,
        @Query("locale") locale: String = LOCALE_ES
    ): Response<CharacterData>

    @GET("character/{realm}/{name}/{character-media}")
    suspend fun getCharacterMedia(
        @Header("Authorization") accessToken: String,
        @Path("name") characterName: String,
        @Path("realm") realm: String?,
        @Path("character-media") characterMedia: String = CHARACTER_MEDIA,
        @Query("namespace") namespace: String = NAMESPACE,
        @Query("locale") locale: String = LOCALE_ES
    ): Response<CharacterMedia>

    @GET("item")
    suspend fun getItemData(
        @Header("Authorization") accessToken: String,
        @Query("name.en_US") itemName: String,
        @Query("namespace") nameSpace: String = STATIC_NAMESPACE,
        @Query("_page") pageNumber: Int = 1,
        @Query("orderby") orderBy: String = "id"
    ): Response<ItemData>

    @GET("item?{itemId}")
    suspend fun getItemMedia(
        @Header("Authorization") accessToken: String,
        @Path("itemId") itemId: Int,
        @Path("item-media") itemMedia: String = ITEM_MEDIA,
        @Query("namespace") namespace: String = STATIC_NAMESPACE
    ): Response<ItemMedia>
}