package com.sergimarrahyarenas.bloodstats.models.characterstatistics

data class Realm(
    val id: Int,
    val key: com.sergimarrahyarenas.bloodstats.models.characterstatistics.Key,
    val name: String,
    val slug: String
)