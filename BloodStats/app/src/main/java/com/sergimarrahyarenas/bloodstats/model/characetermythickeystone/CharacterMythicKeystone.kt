package com.sergimarrahyarenas.bloodstats.model.characetermythickeystone

data class CharacterMythicKeystone(
    val _links: Links,
    val character: Character,
    val current_mythic_rating: CurrentMythicRating,
    val current_period: CurrentPeriod,
    val seasons: List<Season>
)