package com.sergimarrahyarenas.bloodstats.models.characermythickeystoneprofile

data class BestRun(
    val completed_timestamp: Long,
    val dungeon: Dungeon,
    val duration: Int,
    val is_completed_within_time: Boolean,
    val keystone_affixes: List<KeystoneAffixe>,
    val keystone_level: Int,
    val map_rating: MapRating,
    val members: List<Member>,
    val mythic_rating: MythicRating
)