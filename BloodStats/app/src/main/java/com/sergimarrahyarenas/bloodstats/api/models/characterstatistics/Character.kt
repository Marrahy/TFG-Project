package com.sergimarrahyarenas.bloodstats.api.models.characterstatistics

data class Character(
    val id: Int,
    val key: Key,
    val name: String,
    val realm: Realm
)