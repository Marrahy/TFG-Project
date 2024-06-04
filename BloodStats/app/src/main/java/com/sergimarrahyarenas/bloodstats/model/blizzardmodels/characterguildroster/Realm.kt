package com.sergimarrahyarenas.bloodstats.model.blizzardmodels.characterguildroster

data class Realm(
    val id: Int,
    val key: com.sergimarrahyarenas.bloodstats.model.blizzardmodels.characterguildroster.Key,
    val name: String,
    val slug: String
)