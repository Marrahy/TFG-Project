package com.sergimarrahyarenas.bloodstats.model.blizzardmodels.charactermedia

data class Character(
    val id: Int,
    val key: com.sergimarrahyarenas.bloodstats.model.blizzardmodels.charactermedia.Key,
    val name: String,
    val realm: com.sergimarrahyarenas.bloodstats.model.blizzardmodels.charactermedia.Realm
)