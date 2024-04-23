package com.sergimarrahyarenas.api

import com.google.gson.annotations.SerializedName

data class GameDataResponse(
    @SerializedName("id") val id: Int,
    @SerializedName("name") val name: String,
)
