package com.sergimarrahyarenas.bloodstats.model.characterguildroster

data class Guild(
    val faction: Faction,
    val id: Int,
    val key: Key,
    val name: String,
    val realm: Realm
)