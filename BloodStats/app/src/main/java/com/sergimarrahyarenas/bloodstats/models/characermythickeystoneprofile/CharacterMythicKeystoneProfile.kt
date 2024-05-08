package com.sergimarrahyarenas.bloodstats.models.characermythickeystoneprofile

data class CharacterMythicKeystoneProfile(
    val _links: Links,
    val character: Character,
    val current_mythic_rating: CurrentMythicRating,
    val current_period: CurrentPeriod,
    val seasons: List<Season>
)