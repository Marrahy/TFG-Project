package com.sergimarrahyarenas.bloodstats.data.network.api

import com.sergimarrahyarenas.bloodstats.model.blizzardmodels.accesstoken.TokenResponse
import com.sergimarrahyarenas.bloodstats.model.blizzardmodels.characetermythickeystone.CharacterMythicKeystone
import com.sergimarrahyarenas.bloodstats.model.blizzardmodels.characterencounters.CharacterEncounters
import com.sergimarrahyarenas.bloodstats.model.blizzardmodels.characterequipment.CharacterEquipment
import com.sergimarrahyarenas.bloodstats.model.blizzardmodels.characterguildroster.CharacterGuildRoster
import com.sergimarrahyarenas.bloodstats.model.blizzardmodels.charactermedia.CharacterMedia
import com.sergimarrahyarenas.bloodstats.model.blizzardmodels.characterprofilesummary.CharacterProfileSummary
import com.sergimarrahyarenas.bloodstats.model.blizzardmodels.characterspecialization.CharacterSpecialization
import com.sergimarrahyarenas.bloodstats.model.blizzardmodels.characterstatistics.CharacterStatistics
import com.sergimarrahyarenas.bloodstats.model.blizzardmodels.itemdata.ItemData
import com.sergimarrahyarenas.bloodstats.model.blizzardmodels.itemmedia.ItemMedia
import com.sergimarrahyarenas.bloodstats.model.blizzardmodels.realm.Realm
import com.sergimarrahyarenas.bloodstats.model.blizzardmodels.specializationmedia.SpecializationMedia
import com.sergimarrahyarenas.bloodstats.utils.Constants.CHARACTER_MEDIA
import com.sergimarrahyarenas.bloodstats.utils.Constants.DUNGEONS
import com.sergimarrahyarenas.bloodstats.utils.Constants.DYNAMIC_NAMESPACE
import com.sergimarrahyarenas.bloodstats.utils.Constants.ENCOUNTERS
import com.sergimarrahyarenas.bloodstats.utils.Constants.EQUIPMENT
import com.sergimarrahyarenas.bloodstats.utils.Constants.GRANT_TYPE
import com.sergimarrahyarenas.bloodstats.utils.Constants.LOCALE_ES
import com.sergimarrahyarenas.bloodstats.utils.Constants.MYTHIC_KEYSTONE_PROFILE
import com.sergimarrahyarenas.bloodstats.utils.Constants.NAMESPACE
import com.sergimarrahyarenas.bloodstats.utils.Constants.ROSTER
import com.sergimarrahyarenas.bloodstats.utils.Constants.SPECIALIZATION
import com.sergimarrahyarenas.bloodstats.utils.Constants.STATIC_NAMESPACE
import com.sergimarrahyarenas.bloodstats.utils.Constants.STATISTICS
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
        @Query("locale") locale: String
    ): Response<CharacterStatistics>

    @GET("character/{realmSlug}/{name}/{specializations}")
    suspend fun getCharacterSpecialization(
        @Header("Authorization") accessToken: String,
        @Path("name") characterName: String,
        @Path("realmSlug") realmSlug: String,
        @Path("specializations") specializations: String = SPECIALIZATION,
        @Query("namespace") nameSpace: String = NAMESPACE,
        @Query("locale") locale: String
    ): Response<CharacterSpecialization>

    @GET("media/playable-specialization/{specId}")
    suspend fun getCharacterSpecializationMedia(
        @Header("Authorization") accessToken: String,
        @Path("specId") specializationId: Int,
        @Query("namespace") nameSpace: String = STATIC_NAMESPACE,
        @Query("locale") locale: String
    ): Response<SpecializationMedia>

    @GET("character/{realmSlug}/{name}/{equipment}")
    suspend fun getCharacterEquipmentSummary(
        @Header("Authorization") accessToken: String,
        @Path("name") characterName: String,
        @Path("realmSlug") realmSlug: String,
        @Path("equipment") equipment: String = EQUIPMENT,
        @Query("namespace") namespace: String = NAMESPACE,
        @Query("locale") locale: String
    ): Response<CharacterEquipment>

    @GET("guild/{realmSlug}/{nameSlug}/{roster}")
    suspend fun getCharacterGuildRoster(
        @Header("Authorization") accessToken: String,
        @Path("nameSlug") characterName: String,
        @Path("realmSlug") realmSlug: String,
        @Path("roster") roster: String = ROSTER,
        @Query("namespace") nameSpace: String = NAMESPACE,
        @Query("locale") locale: String
    ): Response<CharacterGuildRoster>

    @GET("character/{realmSlug}/{name}/{encounters}/{dungeons}")
    suspend fun getCharacterEncounters(
        @Header("Authorization") accessToken: String,
        @Path("name") characterName: String,
        @Path("realmSlug") realmSlug: String,
        @Path("encounters") encounters: String = ENCOUNTERS,
        @Path("dungeons") dungeons: String = DUNGEONS,
        @Query("namespace") nameSpace: String = NAMESPACE,
        @Query("locale") locale: String
    ): Response<CharacterEncounters>

    @GET("character/{realmSlug}/{name}/{mythic-keystone-profile}")
    suspend fun getCharacterMythicKeystone(
        @Header("Authorization") accessToken: String,
        @Path("name") characterName: String,
        @Path("realmSlug") realmSlug: String,
        @Path("mythic-keystone-profile") mythicKeystoneProfile: String = MYTHIC_KEYSTONE_PROFILE,
        @Query("namespace") nameSpace: String = NAMESPACE,
        @Query("locale") locale: String
    ): Response<CharacterMythicKeystone>

    @GET("character/{realmSlug}/{name}/{character-media}")
    suspend fun getCharacterMedia(
        @Header("Authorization") accessToken: String,
        @Path("name") characterName: String,
        @Path("realmSlug") realm: String?,
        @Path("character-media") characterMedia: String = CHARACTER_MEDIA,
        @Query("namespace") namespace: String = NAMESPACE,
        @Query("locale") locale: String
    ): Response<CharacterMedia>

    @GET("item/{itemId}")
    suspend fun getItemDataById(
        @Header("Authorization") accessToken: String,
        @Path("itemId") itemId: Int,
        @Query("namespace") nameSpace: String = STATIC_NAMESPACE,
        @Query("locale") locale: String
    ): Response<ItemData>

    @GET("media/item/{itemId}")
    suspend fun getItemMedia(
        @Header("Authorization") accessToken: String,
        @Path("itemId") itemId: Int,
        @Query("namespace") namespace: String = STATIC_NAMESPACE,
        @Query("locale") locale: String
    ): Response<ItemMedia>

    @GET("realm/index")
    suspend fun getListOfEURealms(
        @Header("Authorization") accessToken: String,
        @Query("namespace") nameSpace: String = DYNAMIC_NAMESPACE,
        @Query("locale") locale: String
    ): Response<Realm>
}