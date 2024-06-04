package com.sergimarrahyarenas.bloodstats.model.blizzardmodels.characetermythickeystone

data class BestRun(
    val completed_timestamp: Long,
    val dungeon: com.sergimarrahyarenas.bloodstats.model.blizzardmodels.characetermythickeystone.Dungeon,
    val duration: Int,
    val is_completed_within_time: Boolean,
    val keystone_affixes: List<com.sergimarrahyarenas.bloodstats.model.blizzardmodels.characetermythickeystone.KeystoneAffixe>,
    val keystone_level: Int,
    val map_rating: com.sergimarrahyarenas.bloodstats.model.blizzardmodels.characetermythickeystone.MapRating,
    val members: List<com.sergimarrahyarenas.bloodstats.model.blizzardmodels.characetermythickeystone.Member>,
    val mythic_rating: com.sergimarrahyarenas.bloodstats.model.blizzardmodels.characetermythickeystone.MythicRating
)