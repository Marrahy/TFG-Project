package com.sergimarrahyarenas.bloodstats.model.blizzardmodels.characterstatistics

data class Realm(
    val id: Int,
    val key: com.sergimarrahyarenas.bloodstats.model.blizzardmodels.characterstatistics.Key,
    val name: String,
    val slug: String
)