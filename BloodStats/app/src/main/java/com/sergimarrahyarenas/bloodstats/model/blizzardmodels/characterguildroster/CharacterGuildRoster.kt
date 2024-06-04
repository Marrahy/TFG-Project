package com.sergimarrahyarenas.bloodstats.model.blizzardmodels.characterguildroster

data class CharacterGuildRoster(
    val _links: com.sergimarrahyarenas.bloodstats.model.blizzardmodels.characterguildroster.Links,
    val guild: com.sergimarrahyarenas.bloodstats.model.blizzardmodels.characterguildroster.Guild,
    val members: List<com.sergimarrahyarenas.bloodstats.model.blizzardmodels.characterguildroster.Member>
)