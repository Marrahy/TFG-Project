package com.sergimarrahyarenas.bloodstats.model.blizzardmodels.characetermythickeystone

data class CharacterMythicKeystone(
    val _links: com.sergimarrahyarenas.bloodstats.model.blizzardmodels.characetermythickeystone.Links,
    val character: com.sergimarrahyarenas.bloodstats.model.blizzardmodels.characetermythickeystone.Character,
    val current_mythic_rating: com.sergimarrahyarenas.bloodstats.model.blizzardmodels.characetermythickeystone.CurrentMythicRating,
    val current_period: com.sergimarrahyarenas.bloodstats.model.blizzardmodels.characetermythickeystone.CurrentPeriod,
    val seasons: List<com.sergimarrahyarenas.bloodstats.model.blizzardmodels.characetermythickeystone.Season>
)