package com.sergimarrahyarenas.bloodstats.model.blizzardmodels.charactermedia

data class CharacterMedia(
    val _links: com.sergimarrahyarenas.bloodstats.model.blizzardmodels.charactermedia.Links,
    val assets: List<com.sergimarrahyarenas.bloodstats.model.blizzardmodels.charactermedia.Asset>,
    val character: com.sergimarrahyarenas.bloodstats.model.blizzardmodels.charactermedia.Character
)