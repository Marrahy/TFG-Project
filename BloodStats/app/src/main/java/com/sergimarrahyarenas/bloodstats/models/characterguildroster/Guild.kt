package com.sergimarrahyarenas.bloodstats.models.characterguildroster

data class Guild(
    val faction: Faction,
    val id: Int,
    val key: Key,
    val name: String,
    val realm: Realm
)