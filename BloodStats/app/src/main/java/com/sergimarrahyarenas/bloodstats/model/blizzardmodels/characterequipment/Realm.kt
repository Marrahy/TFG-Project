package com.sergimarrahyarenas.bloodstats.model.blizzardmodels.characterequipment

data class Realm(
    val id: Int,
    val key: com.sergimarrahyarenas.bloodstats.model.blizzardmodels.characterequipment.Key,
    val name: String,
    val slug: String
)