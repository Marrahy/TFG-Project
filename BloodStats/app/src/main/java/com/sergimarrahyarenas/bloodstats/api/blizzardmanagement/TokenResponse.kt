package com.sergimarrahyarenas.bloodstats.api.blizzardmanagement

import com.google.gson.annotations.SerializedName

data class TokenResponse(
    @SerializedName("access_token")
    val accessToken: String,
)
