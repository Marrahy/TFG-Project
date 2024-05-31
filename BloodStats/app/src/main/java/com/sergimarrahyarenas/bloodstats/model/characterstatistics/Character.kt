package com.sergimarrahyarenas.bloodstats.model.characterstatistics

data class Character(
    val id: Int,
    val key: com.sergimarrahyarenas.bloodstats.model.characterstatistics.Key,
    val name: String,
    val realm: com.sergimarrahyarenas.bloodstats.model.characterstatistics.Realm
)