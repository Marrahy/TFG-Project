package com.sergimarrahyarenas.bloodstats.model.blizzardmodels.characterspecialization

data class Character(
    val id: Int,
    val key: com.sergimarrahyarenas.bloodstats.model.blizzardmodels.characterspecialization.Key,
    val name: String,
    val realm: com.sergimarrahyarenas.bloodstats.model.blizzardmodels.characterspecialization.Realm
)