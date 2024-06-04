package com.sergimarrahyarenas.bloodstats.model.blizzardmodels.characterstatistics

data class Character(
    val id: Int,
    val key: com.sergimarrahyarenas.bloodstats.model.blizzardmodels.characterstatistics.Key,
    val name: String,
    val realm: com.sergimarrahyarenas.bloodstats.model.blizzardmodels.characterstatistics.Realm
)