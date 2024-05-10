package com.sergimarrahyarenas.bloodstats.api

import com.sergimarrahyarenas.bloodstats.api.Constants.CHARACTER_MEDIA
import com.sergimarrahyarenas.bloodstats.api.Constants.EQUIPMENT
import com.sergimarrahyarenas.bloodstats.api.Constants.GRANT_TYPE
import com.sergimarrahyarenas.bloodstats.api.Constants.ITEM_MEDIA
import com.sergimarrahyarenas.bloodstats.api.Constants.LOCALE_ES
import com.sergimarrahyarenas.bloodstats.api.Constants.MYTHIC_KEYSTONE_PROFILE
import com.sergimarrahyarenas.bloodstats.api.Constants.NAMESPACE
import com.sergimarrahyarenas.bloodstats.api.Constants.ROSTER
import com.sergimarrahyarenas.bloodstats.api.Constants.SPECIALIZATION
import com.sergimarrahyarenas.bloodstats.api.Constants.STATIC_NAMESPACE
import com.sergimarrahyarenas.bloodstats.api.Constants.STATISTICS
import com.sergimarrahyarenas.bloodstats.models.characermythickeystoneprofile.CharacterMythicKeystoneProfile
import com.sergimarrahyarenas.bloodstats.models.characterprofilesummary.CharacterProfileSummary
import com.sergimarrahyarenas.bloodstats.models.characterequipment.CharacterEquipment
import com.sergimarrahyarenas.bloodstats.models.characterguildroster.CharacterGuildRoster
import com.sergimarrahyarenas.bloodstats.models.charactermedia.CharacterMedia
import com.sergimarrahyarenas.bloodstats.models.characterspecialization.CharacterSpecialization
import com.sergimarrahyarenas.bloodstats.models.characterstatistics.CharacterStatistics
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

    @GET("character/{realmSlug}/{name}")
    suspend fun getCharacterProfileSummary(
        @Header("Authorization") accessToken: String,
        @Path("name") characterName: String,
        @Path("realmSlug") realmSlug: String,
        @Query("namespace") namespace: String = NAMESPACE,
        @Query("locale") locale: String = LOCALE_ES
    ): Response<CharacterProfileSummary>

    @GET("character/{realmSlug}/{name}/{statistics}")
    suspend fun getCharacterStatisticsSummary(
        @Header("Authorization") accessToken: String,
        @Path("name") characterName: String,
        @Path("realmSlug") realmSlug: String,
        @Path("statistics") statistics: String = STATISTICS,
        @Query("namespace") namespace: String = NAMESPACE,
        @Query("locale") locale: String = LOCALE_ES
    ): Response<CharacterStatistics>

    @GET("character/{realmSlug}/{name}/{specializations}")
    suspend fun getCharacterSpecialization(
        @Header("Authorization") accessToken: String,
        @Path("name") characterName: String,
        @Path("realmSlug") realmSlug: String,
        @Path("specializations") specializations: String = SPECIALIZATION,
        @Query("namespace") nameSpace: String = NAMESPACE,
        @Query("locale") locale: String = LOCALE_ES
    ): Response<CharacterSpecialization>

    @GET("character/{realmSlug}/{name}/{equipment}")
    suspend fun getCharacterEquipmentSummary(
        @Header("Authorization") accessToken: String,
        @Path("name") characterName: String,
        @Path("realmSlug") realmSlug: String,
        @Path("equipment") equipment: String = EQUIPMENT,
        @Query("namespace") namespace: String = NAMESPACE,
        @Query("locale") locale: String = LOCALE_ES
    ): Response<CharacterEquipment>

    @GET("guild/{realmSlug}/{nameSlug}/{roster}")
    suspend fun getCharacterGuildRoster(
        @Header("Authorization") accessToken: String,
        @Path("nameSlug") characterName: String,
        @Path("realmSlug") realmSlug: String,
        @Path("roster") roster: String = ROSTER,
        @Query("namespace") nameSpace: String = NAMESPACE,
        @Query("locale") locale: String = LOCALE_ES
    ): Response<CharacterGuildRoster>

    @GET("character/{realmSlug}/{name}/{mythic-keystone-profile}")
    suspend fun getCharacterMythicKeystoneProfile(
        @Header("Authorization") accessToken: String,
        @Path("name") characterName: String,
        @Path("realmSlug") realmSlug: String,
        @Path("mythic-keystone-profile") mythicKeystoneProfile: String = MYTHIC_KEYSTONE_PROFILE,
        @Query("namespace") nameSpace: String = NAMESPACE,
        @Query("locale") locale: String = LOCALE_ES
    ): Response<CharacterMythicKeystoneProfile>

    @GET("character/{realmSlug}/{name}/{character-media}")
    suspend fun getCharacterMedia(
        @Header("Authorization") accessToken: String,
        @Path("name") characterName: String,
        @Path("realmSlug") realm: String?,
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

    @GET("item/{itemId}")
    suspend fun getItemMedia(
        @Header("Authorization") accessToken: String,
        @Path("itemId") itemId: Int,
        @Path("item-media") itemMedia: String = ITEM_MEDIA,
        @Query("namespace") namespace: String = STATIC_NAMESPACE
    ): Response<ItemMedia>
}