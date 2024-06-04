package com.sergimarrahyarenas.bloodstats.model.blizzardmodels.characterequipment

data class Character(
    val id: Int,
    val key: com.sergimarrahyarenas.bloodstats.model.blizzardmodels.characterequipment.Key,
    val name: String,
    val realm: com.sergimarrahyarenas.bloodstats.model.blizzardmodels.characterequipment.Realm
)