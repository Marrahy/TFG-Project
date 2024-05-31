package com.sergimarrahyarenas.bloodstats.model.accesstoken

import com.google.gson.annotations.SerializedName

data class TokenResponse(
    @SerializedName("access_token")
    val accessToken: String,
)
