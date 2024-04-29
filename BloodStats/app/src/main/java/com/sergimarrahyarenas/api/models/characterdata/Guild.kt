package com.sergimarrahyarenas.api.models.characterdata

data class Guild(
    val faction: FactionX,
    val id: Int,
    val key: Key,
    val name: String,
    val realm: RealmX
)