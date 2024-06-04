package com.sergimarrahyarenas.bloodstats.model.blizzardmodels.characterguildroster

data class Guild(
    val faction: com.sergimarrahyarenas.bloodstats.model.blizzardmodels.characterguildroster.Faction,
    val id: Int,
    val key: com.sergimarrahyarenas.bloodstats.model.blizzardmodels.characterguildroster.Key,
    val name: String,
    val realm: com.sergimarrahyarenas.bloodstats.model.blizzardmodels.characterguildroster.Realm
)