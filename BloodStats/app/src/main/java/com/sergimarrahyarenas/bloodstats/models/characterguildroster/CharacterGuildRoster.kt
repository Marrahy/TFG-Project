package com.sergimarrahyarenas.bloodstats.models.characterguildroster

data class CharacterGuildRoster(
    val _links: Links,
    val guild: Guild,
    val members: List<Member>
)