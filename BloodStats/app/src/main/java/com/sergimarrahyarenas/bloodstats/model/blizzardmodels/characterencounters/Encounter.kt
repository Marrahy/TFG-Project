package com.sergimarrahyarenas.bloodstats.model.blizzardmodels.characterencounters

data class Encounter(
    val completed_count: Int,
    val encounter: com.sergimarrahyarenas.bloodstats.model.blizzardmodels.characterencounters.EncounterX,
    val last_kill_timestamp: Long
)