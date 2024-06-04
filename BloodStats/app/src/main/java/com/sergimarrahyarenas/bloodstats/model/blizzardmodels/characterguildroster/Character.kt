package com.sergimarrahyarenas.bloodstats.model.blizzardmodels.characterguildroster

data class Character(
    val id: Int,
    val key: com.sergimarrahyarenas.bloodstats.model.blizzardmodels.characterguildroster.Key,
    val level: Int,
    val name: String,
    val playable_class: com.sergimarrahyarenas.bloodstats.model.blizzardmodels.characterguildroster.PlayableClass,
    val playable_race: com.sergimarrahyarenas.bloodstats.model.blizzardmodels.characterguildroster.PlayableRace,
    val realm: com.sergimarrahyarenas.bloodstats.model.blizzardmodels.characterguildroster.RealmX
)