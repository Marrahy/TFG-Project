package com.sergimarrahyarenas.bloodstats.models.characterguildroster

data class Character(
    val id: Int,
    val key: Key,
    val level: Int,
    val name: String,
    val playable_class: PlayableClass,
    val playable_race: PlayableRace,
    val realm: RealmX
)