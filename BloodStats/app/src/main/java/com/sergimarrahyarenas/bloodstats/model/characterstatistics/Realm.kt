package com.sergimarrahyarenas.bloodstats.model.characterstatistics

data class Realm(
    val id: Int,
    val key: com.sergimarrahyarenas.bloodstats.model.characterstatistics.Key,
    val name: String,
    val slug: String
)