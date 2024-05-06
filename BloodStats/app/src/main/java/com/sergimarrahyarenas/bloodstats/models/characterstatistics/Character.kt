package com.sergimarrahyarenas.bloodstats.models.characterstatistics

data class Character(
    val id: Int,
    val key: com.sergimarrahyarenas.bloodstats.models.characterstatistics.Key,
    val name: String,
    val realm: com.sergimarrahyarenas.bloodstats.models.characterstatistics.Realm
)