package com.sergimarrahyarenas.bloodstats.model.characterencounters

data class Encounter(
    val completed_count: Int,
    val encounter: EncounterX,
    val last_kill_timestamp: Long
)