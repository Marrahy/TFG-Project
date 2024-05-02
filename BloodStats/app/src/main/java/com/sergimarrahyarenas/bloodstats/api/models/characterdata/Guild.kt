package com.sergimarrahyarenas.bloodstats.api.models.characterdata

data class Guild(
    val id: Int,
    val key: Key,
    val name: String,
    val realm: RealmX
)